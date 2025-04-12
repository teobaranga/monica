package com.teobaranga.monica.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.log.LoggerOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ForScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class AndroidAppComponent(
    /**
     * The Android application that is provided to this object graph.
     */
    @get:Provides val application: Application,
) : CoilOwner, WorkerFactoryOwner, LoggerOwner {

    @ForScope(AppScope::class)
    abstract val viewModelFactory: ViewModelProvider.Factory

    @Provides
    @ApplicationContext
    fun provideContext(): Context = application
}
