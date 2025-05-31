package com.teobaranga.monica.sync

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.diamondedge.logging.logging
import com.teobaranga.monica.account.settings.getTokenStorage
import com.teobaranga.monica.core.account.AccountListener
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.user.data.local.IUserRepository
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject

@Inject
internal class SyncUseCase(
    private val dataStore: DataStore<Preferences>,
    private val userRepository: IUserRepository,
    private val genderRepository: GenderRepository,
    private val accountListeners: Set<AccountListener>,
) {
    suspend operator fun invoke(): Result<Unit> {
        val isTokenAvailable = dataStore.data.first().getTokenStorage().accessToken != null
        if (!isTokenAvailable) {
            log.d { "Access token not available, skipping sync" }
            return Result.success(Unit)
        }

        accountListeners.forEach { accountListener ->
            accountListener.onSignedIn()
        }

        log.d { "Syncing current user" }
        userRepository.sync()

        log.d { "Syncing genders" }
        // TODO is this the best place? probably not
        genderRepository.fetchLatestGenders()

        return Result.success(Unit)
    }

    companion object {
        private val log = logging()
    }
}
