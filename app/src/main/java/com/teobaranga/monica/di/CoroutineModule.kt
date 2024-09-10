package com.teobaranga.monica.di

import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoroutineModule {

    companion object {

        @[Provides Singleton ApplicationContext]
        fun provideAppCoroutineScope(dispatcher: Dispatcher): CoroutineScope {
            return CoroutineScope(SupervisorJob() + dispatcher.default)
        }
    }
}
