package com.teobaranga.monica.work

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class TestWorkScheduler : WorkScheduler {

    override fun schedule(workName: String) {
        // TODO
    }
}
