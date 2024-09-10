package com.teobaranga.monica.core.dispatcher

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

interface Dispatcher {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideDispatcher(
        @DefaultDispatcher
        defaultDispatcher: CoroutineDispatcher,
        @MainDispatcher
        mainDispatcher: CoroutineDispatcher,
        @IoDispatcher
        ioDispatcher: CoroutineDispatcher,
    ): Dispatcher {
        return object : Dispatcher {
            override val default = defaultDispatcher
            override val main = mainDispatcher
            override val io = ioDispatcher
        }
    }
}
