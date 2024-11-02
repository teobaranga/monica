package com.teobaranga.monica.sync

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.teobaranga.monica.account.AccountListener
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.settings.getTokenStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted
    appContext: Context,
    @Assisted
    workerParams: WorkerParameters,
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository,
    private val genderRepository: GenderRepository,
    private val accountListeners: Set<@JvmSuppressWildcards AccountListener>,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val isTokenAvailable = dataStore.data.first().getTokenStorage().accessToken != null
        if (!isTokenAvailable) {
            Timber.d("Access token not available, skipping sync")
            return Result.success()
        }

        accountListeners.forEach { accountListener ->
            accountListener.onSignedIn()
        }

        Timber.d("Syncing current user")
        userRepository.sync()

        Timber.d("Syncing genders")
        // TODO is this the best place? probably not
        genderRepository.fetchLatestGenders()

        return Result.success()
    }

    companion object {

        // This name should not be changed otherwise the app may have concurrent sync requests running
        private const val SYNC_WORK_NAME = "SyncWorkName"

        private val SyncConstraints
            get() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        fun enqueue(workManager: WorkManager) {
            workManager
                .enqueueUniqueWork(
                    SYNC_WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    startUpSyncWork(),
                )
        }

        private fun startUpSyncWork(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<SyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(SyncConstraints)
                .build()
        }
    }
}
