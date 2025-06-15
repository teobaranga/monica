package com.teobaranga.monica.work

import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import com.teobaranga.monica.sync.SyncUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

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
