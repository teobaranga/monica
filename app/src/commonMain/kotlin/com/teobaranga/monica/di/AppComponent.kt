package com.teobaranga.monica.di

import androidx.lifecycle.ViewModelProvider
import com.teobaranga.monica.log.LoggerOwner
import com.teobaranga.monica.ui.CoilOwner
import com.teobaranga.monica.work.WorkerFactoryOwner
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ForScope

expect abstract class AppComponent : CoilOwner, WorkerFactoryOwner, LoggerOwner {
    @ForScope(AppScope::class)
    abstract val viewModelFactory: ViewModelProvider.Factory
}

expect fun createAppComponent(): AppComponent
