package com.teobaranga.monica.work

import androidx.work.WorkManager
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import com.teobaranga.monica.sync.SyncWorker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
class WorkManagerWorkScheduler(
    private val workManager: WorkManager,
) : WorkScheduler {

    override fun schedule(workName: String) {
        when (workName) {
            SYNC_WORKER_WORK_NAME -> SyncWorker.enqueue(workManager)
            else -> error("Unknown work name: $workName")
        }
    }
}
