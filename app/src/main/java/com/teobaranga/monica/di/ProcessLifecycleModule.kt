package com.teobaranga.monica.di

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
interface ProcessLifecycleModule {

    companion object {

        @Provides
        @ApplicationContext
        fun bindLifecycleScope(): CoroutineScope {
            return ProcessLifecycleOwner.get().lifecycleScope
        }
    }
}
