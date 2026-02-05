package com.teobaranga.monica.work

import androidx.work.WorkManager
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import com.teobaranga.monica.sync.SyncWorker
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

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
