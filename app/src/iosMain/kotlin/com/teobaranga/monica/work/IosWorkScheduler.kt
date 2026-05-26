package com.teobaranga.monica.work

import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import com.teobaranga.monica.sync.SyncUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class IosWorkScheduler internal constructor(
    private val sync: SyncUseCase,
) : WorkScheduler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun schedule(workName: String) {
        when (workName) {
            SYNC_WORKER_WORK_NAME -> {
                // TODO implement a WorkManager equivalent on iOS
                scope.launch {
                    sync()
                }
            }

            else -> error("Unknown work name: $workName")
        }
    }
}
