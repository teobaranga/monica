package com.teobaranga.monica.di

import coil3.PlatformContext
import com.teobaranga.monica.log.LoggerOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
abstract class AppComponent : CoilOwner, WorkerFactoryOwner, LoggerOwner, ViewModelGraph {

    @Provides
    fun providePlatformContext(): PlatformContext = PlatformContext.INSTANCE
}
