package com.teobaranga.monica.work

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
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
