package com.teobaranga.monica.di

import android.app.Application
import android.content.Context
import androidx.work.WorkerFactory
import coil3.PlatformContext
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.log.LoggerOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface AppComponent : CoilOwner, WorkerFactoryOwner, LoggerOwner, ViewModelGraph {

    val workerFactory: WorkerFactory

    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context = application

    @Provides
    fun providePlatformContext(@ApplicationContext context: Context): PlatformContext = context

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppComponent
    }
}
