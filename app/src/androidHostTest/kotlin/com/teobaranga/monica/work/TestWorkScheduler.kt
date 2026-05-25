package com.teobaranga.monica.work

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    replaces = [WorkManagerWorkScheduler::class],
)
class TestWorkScheduler : WorkScheduler {

    private val scheduledWork = mutableSetOf<String>()

    override fun schedule(workName: String) {
        scheduledWork.add(workName)
    }

    fun isWorkScheduled(workName: String): Boolean {
        return scheduledWork.contains(workName)
    }

    fun clearWork() {
        scheduledWork.clear()
    }
}
