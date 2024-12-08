package com.teobaranga.monica.core.dispatcher

import com.r0adkll.kimchi.annotations.ContributesBinding
import com.r0adkll.kimchi.annotations.ContributesTo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

interface Dispatcher {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

typealias DefaultCoroutineDispatcher = CoroutineDispatcher
typealias MainCoroutineDispatcher = CoroutineDispatcher
typealias IoCoroutineDispatcher = CoroutineDispatcher

@me.tatarka.inject.annotations.Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DispatcherImpl(
    override val default: DefaultCoroutineDispatcher,
    override val main: MainCoroutineDispatcher,
    override val io: IoCoroutineDispatcher,
) : Dispatcher

@ContributesTo(AppScope::class)
interface DispatcherComponent {

    @me.tatarka.inject.annotations.Provides
    fun providesDefaultDispatcher(): DefaultCoroutineDispatcher = Dispatchers.Default

    @me.tatarka.inject.annotations.Provides
    fun providesMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @me.tatarka.inject.annotations.Provides
    fun providesIoDispatcher(): IoCoroutineDispatcher = Dispatchers.IO
}
