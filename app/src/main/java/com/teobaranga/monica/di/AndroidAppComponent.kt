package com.teobaranga.monica.di

import android.app.Application
import android.content.Context
import com.teobaranga.monica.inject.runtime.ApplicationContext
import com.teobaranga.monica.inject.runtime.ViewModelFactoryOwner
import com.teobaranga.monica.log.TimberOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class AndroidAppComponent(
    /**
     * The Android application that is provided to this object graph.
     */
    @get:Provides val application: Application,
) : CoilOwner, ViewModelFactoryOwner, WorkerFactoryOwner, TimberOwner {

    @Provides
    @ApplicationContext
    fun provideContext(): Context = application
}