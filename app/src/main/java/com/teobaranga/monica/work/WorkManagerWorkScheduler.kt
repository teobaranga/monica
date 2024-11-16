package com.teobaranga.monica.work

import androidx.work.WorkManager
import com.teobaranga.monica.sync.SyncWorker
import javax.inject.Inject

class WorkManagerWorkScheduler @Inject constructor(
    private val workManager: WorkManager,
) : WorkScheduler {

    override fun schedule(workName: String) {
        when (workName) {
            SyncWorker.WORK_NAME -> SyncWorker.enqueue(workManager)
            else -> error("Unknown work name: $workName")
        }
    }
}
