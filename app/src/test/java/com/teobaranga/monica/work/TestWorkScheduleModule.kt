package com.teobaranga.monica.work

import dagger.Binds
import dagger.Module
import javax.inject.Inject

class TestWorkScheduler @Inject constructor() : WorkScheduler {

    override fun schedule(workName: String) {
        // TODO
    }
}

@Module
abstract class TestWorkScheduleModule {

    @Binds
    abstract fun bindWorkScheduler(workScheduler: TestWorkScheduler): WorkScheduler
}
