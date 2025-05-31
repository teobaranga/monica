package com.teobaranga.monica.core.paging

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual fun uiDispatcher(): CoroutineContext = Dispatchers.Main
