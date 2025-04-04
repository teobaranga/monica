package com.teobaranga.monica.core.ui.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScrollToTopEffect(
    lazyListState: LazyListState,
    getFirstId: () -> Int?,
) {
    var prevId by rememberSaveable { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        prevId = getFirstId()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val firstId = getFirstId()
        if (prevId != null && prevId != firstId) {
            coroutineScope.launch {
                delay(250)
                lazyListState.animateScrollToItem(0)
            }
        }
    }
}
