package com.teobaranga.monica

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.dispatcher.DispatcherImpl
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(
    scope = AppScope::class,
    replaces = [DispatcherImpl::class],
)
class TestDispatcher : Dispatcher {
    override val main = UnconfinedTestDispatcher()
    override val io = UnconfinedTestDispatcher()
    override val default = UnconfinedTestDispatcher()
}
