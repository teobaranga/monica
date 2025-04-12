package com.teobaranga.monica.setup.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.diamondedge.logging.logging
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TokenRequest
import com.teobaranga.monica.sync.SyncWorker
import com.teobaranga.monica.work.WorkScheduler
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class SignInUseCase internal constructor(
    private val dispatcher: Dispatcher,
    private val monicaApi: MonicaApi,
    private val dataStore: DataStore<Preferences>,
    private val workScheduler: WorkScheduler,
) {

    suspend operator fun invoke(clientId: String, clientSecret: String, authorizationCode: String): Boolean {
        return withContext(dispatcher.io) {
            // Fetch the access token
            val tokenResponse = monicaApi.getAccessToken(TokenRequest(clientId, clientSecret, authorizationCode))
                .onFailure {
                    log.w { "Failed to get access token: ${message()}" }
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

            // Trigger a sync which loads the current user and any other data
            workScheduler.schedule(SyncWorker.WORK_NAME)

            true
        }
    }

    companion object {
        private val log = logging()
    }
}
