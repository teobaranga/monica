package com.teobaranga.monica.setup

import com.teobaranga.monica.TestDispatcher
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.Binds
import dagger.Module

@Module
abstract class DispatcherModule {

    @Binds
    abstract fun provideDispatcher(dispatcher: TestDispatcher): Dispatcher
}
