package com.teobaranga.monica.work

import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class IosWorkScheduler() : WorkScheduler {

    override fun schedule(workName: String) {
        when (workName) {
            SYNC_WORKER_WORK_NAME -> {
                // TODO
            }

            else -> error("Unknown work name: $workName")
        }
    }
}
