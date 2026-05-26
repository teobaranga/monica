package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.dispatcher.DispatcherImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.test.UnconfinedTestDispatcher

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
