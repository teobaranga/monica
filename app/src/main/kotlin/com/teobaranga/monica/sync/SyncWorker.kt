package com.teobaranga.monica.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
internal class SyncWorker(
    @Assisted
    appContext: Context,
    @Assisted
    workerParams: WorkerParameters,
    private val sync: SyncUseCase,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork() = sync()

    companion object {

        // This name should not be changed otherwise the app may have concurrent sync requests running
        const val WORK_NAME = "SyncWorkName"

        private val SyncConstraints
            get() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        fun enqueue(workManager: WorkManager) {
            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = WORK_NAME,
                    existingWorkPolicy = ExistingWorkPolicy.KEEP,
                    request = startUpSyncWork(),
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
