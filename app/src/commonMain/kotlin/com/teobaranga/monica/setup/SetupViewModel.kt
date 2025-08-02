package com.teobaranga.monica.setup

import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.diamondedge.logging.logging
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.auth.AuthorizationRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.settings.getOAuthSettings
import com.teobaranga.monica.settings.oAuthSettings
import com.teobaranga.monica.setup.domain.SignInResult
import com.teobaranga.monica.setup.domain.SignInUseCase
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@OptIn(SavedStateHandleSaveableApi::class)
@Inject
@ContributesViewModel(AppScope::class, assistedFactory = SetupViewModel.Factory::class)
class SetupViewModel(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    authorizationRepository: AuthorizationRepository,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn

    val uiState by savedStateHandle.saveable(saver = UiState.Saver) {
        UiState()
    }

    private val _setupUri = MutableSharedFlow<String>()
    val setupUri: SharedFlow<String> = _setupUri

    init {
        viewModelScope.launch(dispatcher.io) {
            val preferences = dataStore.data.first()
            val oAuthSettings = preferences.getOAuthSettings()
            withContext(dispatcher.main) {
                oAuthSettings.serverAddress?.let {
                    uiState.onServerAddressChanged(TextFieldValue(it))
                }
                oAuthSettings.clientId?.let {
                    uiState.onClientIdChanged(TextFieldValue(it))
                }
                oAuthSettings.clientSecret?.let {
                    uiState.onClientSecretChanged(TextFieldValue(it))
                }
            }
        }
    }

    fun onSignIn() {
        viewModelScope.launch(dispatcher.io) {
            val serverAddress = URLBuilder(uiState.serverAddress.text)
            val baseUrl = serverAddress
                .appendPathSegments("oauth")
                .appendPathSegments("authorize")
                .build()

            dataStore.edit { preferences ->
                preferences.oAuthSettings {
                    setServerAddress(uiState.serverAddress.text)
                    setClientId(uiState.clientId.text)
                    setClientSecret(uiState.clientSecret.text)
                }
            }

            val url = URLBuilder(baseUrl).apply {
                parameters.apply {
                    append(PARAM_CLIENT_ID, uiState.clientId.text)
                    append(PARAM_RESPONSE_TYPE, "code")
                    append(PARAM_REDIRECT_URI, REDIRECT_URI)
                }
            }
                .build()
                .toString()
            _setupUri.emit(url)
        }
    }

    fun onAuthorizationCode(code: String?) {
        if (code == null) {
            log.w { "Received null authorization code" }
            return
        }
        log.d { "Authorization code: $code" }
        viewModelScope.launch(dispatcher.io) {
            withContext(dispatcher.main) {
                uiState.isSigningIn = true
            }
            val oAuthSettings = dataStore.data.first().getOAuthSettings()
            val clientId = requireNotNull(oAuthSettings.clientId)
            val clientSecret = requireNotNull(oAuthSettings.clientSecret)
            val signInResult = signInUseCase(clientId, clientSecret, code)
            when (signInResult) {
                is SignInResult.Error.UnknownError -> {
                    uiState.error = UiState.Error.ConfigurationError
                    withContext(dispatcher.main) {
                        uiState.isSigningIn = false
                    }
                }
                is SignInResult.Success -> {
                    // nothing to do
                }
            }
        }
    }

    companion object {
        private val log = logging()
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): SetupViewModel
    }
}
