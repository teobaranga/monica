package com.teobaranga.monica.log

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimberTreeModule {

    @Provides
    @Singleton
    fun provideDebugTrees(): Set<Timber.Tree> = emptySet()
}
