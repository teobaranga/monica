package com.teobaranga.monica

import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcher @Inject constructor() : Dispatcher {
    override val main = UnconfinedTestDispatcher()
    override val io = UnconfinedTestDispatcher()
    override val default = UnconfinedTestDispatcher()
}
