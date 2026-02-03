package com.teobaranga.monica.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.teobaranga.monica.R
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

private const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "SyncNotificationChannel"

@Inject
internal class SyncWorker(
    @Assisted
    private val appContext: Context,
    @Assisted
    workerParams: WorkerParameters,
    private val sync: SyncUseCase,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val syncResult = sync()
        return if (syncResult.isSuccess) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            /* notificationId = */ SYNC_NOTIFICATION_ID,
            /* notification = */ appContext.syncWorkNotification(),
        )
    }

    /**
     * Notification displayed on lower API levels when sync workers are being
     * run with a foreground service
     */
    private fun Context.syncWorkNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                /* id = */ SYNC_NOTIFICATION_CHANNEL_ID,
                /* name = */ getString(R.string.sync_work_notification_channel_name),
                /* importance = */ NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = getString(R.string.sync_work_notification_channel_description)
            }
            // Register the channel with the system
            val notificationManager = getSystemService<NotificationManager>()

            notificationManager?.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(
            /* context = */ this,
            /* channelId = */ SYNC_NOTIFICATION_CHANNEL_ID,
        )
            .setSmallIcon(R.drawable.ic_monica)
            .setContentTitle(getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    companion object {

        private val SyncConstraints
            get() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        fun enqueue(workManager: WorkManager) {
            workManager
                .enqueueUniqueWork(
                    uniqueWorkName = SYNC_WORKER_WORK_NAME,
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
