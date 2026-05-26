package com.teobaranga.monica.core.dispatcher

import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface CoroutineComponent {

    @Provides
    @ApplicationContext
    @SingleIn(AppScope::class)
    fun provideAppCoroutineScope(dispatcher: Dispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher.default)
    }
}
