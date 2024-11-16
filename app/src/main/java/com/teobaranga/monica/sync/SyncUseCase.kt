package com.teobaranga.monica.sync

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.ListenableWorker
import com.teobaranga.monica.account.AccountListener
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.settings.getTokenStorage
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import timber.log.Timber

internal class SyncUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository,
    private val genderRepository: GenderRepository,
    private val accountListeners: Set<@JvmSuppressWildcards AccountListener>,
) {
    suspend operator fun invoke(): ListenableWorker.Result {
        val isTokenAvailable = dataStore.data.first().getTokenStorage().accessToken != null
        if (!isTokenAvailable) {
            Timber.d("Access token not available, skipping sync")
            return ListenableWorker.Result.success()
        }

        accountListeners.forEach { accountListener ->
            accountListener.onSignedIn()
        }

        Timber.d("Syncing current user")
        userRepository.sync()

        Timber.d("Syncing genders")
        // TODO is this the best place? probably not
        genderRepository.fetchLatestGenders()

        return ListenableWorker.Result.success()
    }
}
