package com.teobaranga.monica.core.paging

import androidx.compose.ui.platform.AndroidUiDispatcher
import kotlin.coroutines.CoroutineContext

actual fun uiDispatcher(): CoroutineContext = AndroidUiDispatcher.Main
