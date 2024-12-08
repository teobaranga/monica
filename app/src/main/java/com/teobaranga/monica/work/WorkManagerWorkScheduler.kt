package com.teobaranga.monica.work

import androidx.work.WorkManager
import com.teobaranga.monica.sync.SyncWorker
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import com.r0adkll.kimchi.annotations.ContributesBinding

@me.tatarka.inject.annotations.Inject
@ContributesBinding(AppScope::class)
class WorkManagerWorkScheduler(
    private val workManager: WorkManager,
) : WorkScheduler {

    override fun schedule(workName: String) {
        when (workName) {
            SyncWorker.WORK_NAME -> SyncWorker.enqueue(workManager)
            else -> error("Unknown work name: $workName")
        }
    }
}
