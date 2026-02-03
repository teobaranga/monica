package com.teobaranga.monica.setup.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.diamondedge.logging.logging
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TokenErrorResponse
import com.teobaranga.monica.data.TokenRequest
import com.teobaranga.monica.work.WorkScheduler
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

// This name should not be changed otherwise the app may have concurrent sync requests running
const val SYNC_WORKER_WORK_NAME = "SyncWorkName"

@Inject
class SignInUseCase(
    private val dispatcher: Dispatcher,
    private val monicaApi: MonicaApi,
    private val dataStore: DataStore<Preferences>,
    private val workScheduler: WorkScheduler,
) {

    suspend operator fun invoke(clientId: String, clientSecret: String, authorizationCode: String): SignInResult {
        return withContext(dispatcher.io) {
            // Fetch the access token
            val tokenResponse = monicaApi.getAccessToken(TokenRequest(clientId, clientSecret, authorizationCode))

            when (tokenResponse) {
                is ApiResponse.Failure.Error -> {
                    log.e { "Failed to get access token: ${tokenResponse.message()}" }
                    val tokenErrorResponse = (tokenResponse.payload as? HttpResponse)?.body<TokenErrorResponse>()
                    if (tokenErrorResponse != null) {
                        SignInResult.Error.ServerError(tokenErrorResponse.message)
                    } else {
                        SignInResult.Error.UnknownError
                    }
                }

                is ApiResponse.Failure.Exception -> {
                    log.e(tokenResponse.throwable) { "Sign in exception" }
                    SignInResult.Error.UnknownError
                }

                is ApiResponse.Success -> {
                    // Store the token
                    dataStore.edit { preferences ->
                        preferences.tokenStorage {
                            setAuthorizationCode(authorizationCode)
                            setAccessToken(tokenResponse.data.accessToken)
                            setRefreshToken(tokenResponse.data.refreshToken)
                        }
                    }

                    // Trigger a sync which loads the current user and any other data
                    workScheduler.schedule(SYNC_WORKER_WORK_NAME)

                    SignInResult.Success
                }
            }
        }
    }

    companion object {
        private val log = logging()
    }
}
