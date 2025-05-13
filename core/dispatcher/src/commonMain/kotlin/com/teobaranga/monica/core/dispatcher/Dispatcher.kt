package com.teobaranga.monica.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

typealias DefaultCoroutineDispatcher = CoroutineDispatcher
typealias MainCoroutineDispatcher = CoroutineDispatcher
typealias IoCoroutineDispatcher = CoroutineDispatcher

interface Dispatcher {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DispatcherImpl(
    override val default: DefaultCoroutineDispatcher,
    override val main: MainCoroutineDispatcher,
    override val io: IoCoroutineDispatcher,
) : Dispatcher
