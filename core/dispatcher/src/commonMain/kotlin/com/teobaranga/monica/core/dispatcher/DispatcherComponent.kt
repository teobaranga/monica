package com.teobaranga.monica.core.dispatcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface DispatcherComponent {

    @Provides
    fun providesDefaultDispatcher(): DefaultCoroutineDispatcher = Dispatchers.Default

    @Provides
    fun providesMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @Provides
    fun providesIoDispatcher(): IoCoroutineDispatcher = Dispatchers.IO
}
