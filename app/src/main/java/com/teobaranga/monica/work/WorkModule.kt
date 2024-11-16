package com.teobaranga.monica.work

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkModule {

    @Binds
    abstract fun bindWorkScheduler(workManagerWorkScheduler: WorkManagerWorkScheduler): WorkScheduler
}
