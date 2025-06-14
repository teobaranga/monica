package com.teobaranga.monica.di

import androidx.lifecycle.ViewModelProvider
import coil3.PlatformContext
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
actual abstract class AppComponent : CoilOwner, WorkerFactoryOwner, LoggerOwner {

    @ForScope(AppScope::class)
    actual abstract val viewModelFactory: ViewModelProvider.Factory

    @Provides
    fun providePlatformContext(): PlatformContext = PlatformContext.INSTANCE
}
