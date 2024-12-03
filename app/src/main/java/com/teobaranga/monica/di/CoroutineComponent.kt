package com.teobaranga.monica.di

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.inject.runtime.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface CoroutineComponent {

    @Provides
    @ApplicationContext
    @SingleIn(AppScope::class)
    fun provideAppCoroutineScope(dispatcher: Dispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher.default)
    }
}
