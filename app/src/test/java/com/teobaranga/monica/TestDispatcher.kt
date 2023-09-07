package com.teobaranga.monica

import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcher : Dispatcher {
    override val main = UnconfinedTestDispatcher()
    override val io = UnconfinedTestDispatcher()
}
