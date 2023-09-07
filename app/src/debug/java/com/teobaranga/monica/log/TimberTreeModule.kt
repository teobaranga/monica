package com.teobaranga.monica.log

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimberTreeModule {

    @Provides
    @Singleton
    @IntoSet
    fun provideDebugTree(): Timber.Tree = Timber.DebugTree()
}
