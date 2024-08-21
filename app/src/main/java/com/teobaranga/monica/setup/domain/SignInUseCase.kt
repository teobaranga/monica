package com.teobaranga.monica.setup.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TokenRequest
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.settings.tokenStorage
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SignInUseCase @Inject internal constructor(
    private val dispatcher: Dispatcher,
    private val monicaApi: MonicaApi,
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(clientId: String, clientSecret: String, authorizationCode: String): Boolean {
        return withContext(dispatcher.io) {
            // Fetch the access token
            val tokenResponse = monicaApi.getAccessToken(TokenRequest(clientId, clientSecret, authorizationCode))
                .onFailure {
                    Timber.w("Failed to get access token: %s", this)
                }
                .getOrNull() ?: return@withContext false

            // Store the token
            dataStore.edit { preferences ->
                preferences.tokenStorage {
                    setAuthorizationCode(authorizationCode)
                    setAccessToken(tokenResponse.accessToken)
                    setRefreshToken(tokenResponse.refreshToken)
                }
            }

            // Store the current user, marking the sign in as complete
            try {
                userRepository.sync()
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync user")
                return@withContext false
            }

            true
        }
    }
}
