package com.teobaranga.monica.util.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

interface Dispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideDispatcher(
        @MainDispatcher
        mainDispatcher: CoroutineDispatcher,
        @IoDispatcher
        ioDispatcher: CoroutineDispatcher,
    ): Dispatcher {
        return object : Dispatcher {
            override val main = mainDispatcher
            override val io = ioDispatcher
        }
    }
}
