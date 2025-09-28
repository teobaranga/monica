package com.teobaranga.monica.setup

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.teobaranga.monica.setup.domain.ValidateServerAddressUseCase
import io.ktor.http.appendPathSegments
import io.ktor.http.buildUrl
import io.ktor.http.isSecure
import io.ktor.http.takeFrom
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class SetupViewModel(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    authorizationRepository: AuthorizationRepository,
    private val validateServerAddress: ValidateServerAddressUseCase,
    private val signIn: SignInUseCase,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn

    val uiState = flow {
        val preferences = dataStore.data.first()
        val oAuthSettings = preferences.getOAuthSettings()
        val uiState = UiState()
        oAuthSettings.serverAddress?.let {
            uiState.serverAddress.setTextAndPlaceCursorAtEnd(it)
        }
        oAuthSettings.clientId?.let {
            uiState.clientId.setTextAndPlaceCursorAtEnd(it)
        }
        oAuthSettings.clientSecret?.let {
            uiState.clientSecret.setTextAndPlaceCursorAtEnd(it)
        }
        emit(uiState)
    }.onEach {
        setupUiJob(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UiState(),
    )

    private val _setupEvents = MutableSharedFlow<SetupEvent>()
    val setupEvents = _setupEvents.asSharedFlow()

    private var uiJob: Job? = null

    fun onSignIn() {
        viewModelScope.launch(dispatcher.io) {
            val address = uiState.value.serverAddress.text.toString().trim()
            val clientId = uiState.value.clientId.text.toString().trim()
            val clientSecret = uiState.value.clientSecret.text.toString().trim()

            dataStore.edit { preferences ->
                preferences.oAuthSettings {
                    setServerAddress(address)
                    setClientId(clientId)
                    setClientSecret(clientSecret)
                }
            }

            when (validateServerAddress(address)) {
                ValidateServerAddressUseCase.Result.Invalid -> {
                    uiState.value.error = UiState.Error.ServerAddressInvalidError
                    return@launch
                }

                ValidateServerAddressUseCase.Result.InvalidProtocol -> {
                    uiState.value.error = UiState.Error.ServerAddressProtocolError
                    return@launch
                }

                ValidateServerAddressUseCase.Result.Valid -> {
                    val url = buildUrl {
                        takeFrom(address)
                        appendPathSegments("oauth", "authorize")
                        parameters.apply {
                            append(PARAM_CLIENT_ID, uiState.value.clientId.text.toString())
                            append(PARAM_RESPONSE_TYPE, "code")
                            append(PARAM_REDIRECT_URI, REDIRECT_URI)
                        }
                    }

                    _setupEvents.emit(
                        SetupEvent.Login(
                            setupUrl = url.toString(),
                            isSecure = url.protocol.isSecure(),
                        )
                    )
                }
            }
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
                uiState.value.isSigningIn = true
            }
            val oAuthSettings = dataStore.data.first().getOAuthSettings()
            val clientId = requireNotNull(oAuthSettings.clientId)
            val clientSecret = requireNotNull(oAuthSettings.clientSecret)
            val signInResult = signIn(clientId, clientSecret, code)
            when (signInResult) {
                is SignInResult.Error.ServerError -> {
                    uiState.value.error = UiState.Error.ConfigurationError(message = signInResult.message)
                }

                is SignInResult.Error.UnknownError -> {
                    uiState.value.error = UiState.Error.ConfigurationError(message = null)
                }

                is SignInResult.Success -> {
                    // nothing to do
                }
            }
            withContext(dispatcher.main) {
                uiState.value.isSigningIn = false
            }
        }
    }

    private fun setupUiJob(uiState: UiState) {
        uiJob?.cancel()
        uiJob = viewModelScope.launch {
            launch {
                snapshotFlow { uiState.serverAddress.text }
                    .collect {
                        if (uiState.error == UiState.Error.ServerAddressProtocolError
                            || uiState.error == UiState.Error.ServerAddressInvalidError
                        ) {
                            uiState.error = null
                        }
                    }
            }
            launch {
                merge(
                    snapshotFlow { uiState.serverAddress.text },
                    snapshotFlow { uiState.clientId.text },
                    snapshotFlow { uiState.clientSecret.text },
                ).collect {
                    if (uiState.error is UiState.Error.ConfigurationError) {
                        uiState.error = null
                    }
                }
            }
        }
    }

    companion object {
        private val log = logging()
    }
}
