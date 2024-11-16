package com.teobaranga.monica.setup.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TokenRequest
import com.teobaranga.monica.settings.tokenStorage
import com.teobaranga.monica.sync.SyncWorker
import com.teobaranga.monica.work.WorkScheduler
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SignInUseCase @Inject internal constructor(
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

            // Trigger a sync which loads the current user and any other data
            workScheduler.schedule(SyncWorker.WORK_NAME)

            true
        }
    }
}
