package com.teobaranga.monica.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TokenRequest
import com.teobaranga.monica.settings.getTokenStorage
import com.teobaranga.monica.settings.tokenStorage
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

interface AuthorizationRepository {

    val isLoggedIn: StateFlow<Boolean?>

    suspend fun signIn(clientId: String, clientSecret: String, authorizationCode: String): Boolean
}

@Singleton
class MonicaAuthorizationRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    private val monicaApi: Provider<MonicaApi>,
) : AuthorizationRepository {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    override val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        scope.launch {
            dataStore.data
                .collectLatest { preferences ->
                    val tokenStorage = preferences.getTokenStorage()
                    withContext(dispatcher.main) {
                        _isLoggedIn.emit(tokenStorage.authorizationCode != null)
                    }
                }
        }
    }

    override suspend fun signIn(clientId: String, clientSecret: String, authorizationCode: String): Boolean {
        val tokenResponse = monicaApi.get().getAccessToken(TokenRequest(clientId, clientSecret, authorizationCode))
            .onFailure {
                Timber.w("Failed to get access token: %s", this)
            }
            .getOrNull() ?: return false
        dataStore.edit { preferences ->
            preferences.tokenStorage {
                setAuthorizationCode(authorizationCode)
                setAccessToken(tokenResponse.accessToken)
                setRefreshToken(tokenResponse.refreshToken)
            }
        }
        return true
    }
}
