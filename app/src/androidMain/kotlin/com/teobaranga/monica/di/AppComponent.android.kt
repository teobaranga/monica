package com.teobaranga.monica.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import coil3.PlatformContext
import com.teobaranga.monica.MonicaApp
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.log.LoggerOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ForScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual fun createAppComponent(): AppComponent = AppComponent::class.create(MonicaApp.instance)

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
actual abstract class AppComponent(
    /**
     * The Android application that is provided to this object graph.
     */
    @get:Provides val application: Application,
) : CoilOwner, WorkerFactoryOwner, LoggerOwner {

    @ForScope(AppScope::class)
    actual abstract val viewModelFactory: ViewModelProvider.Factory

    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun providePlatformContext(@ApplicationContext context: Context): PlatformContext = context
}
