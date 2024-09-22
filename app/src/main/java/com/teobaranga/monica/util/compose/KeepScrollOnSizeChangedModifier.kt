package com.teobaranga.monica.util.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

@Composable
fun Modifier.keepScrollOnSizeChanged(lazyListState: LazyListState): Modifier {
    var offset by rememberSaveable { mutableIntStateOf(lazyListState.firstVisibleItemScrollOffset) }
    var size by remember { mutableStateOf<IntSize?>(null) }
    var prevSize by remember { mutableStateOf(size) }
    // Restore the offset when the screen is resumed, only as it is shrinking
    LaunchedEffect(Unit) {
        snapshotFlow { size }
            .filterNotNull()
            .onEach { newSize ->
                val isShrinking = newSize.height < (prevSize?.height ?: Int.MAX_VALUE)
                if (isShrinking) {
                    with(lazyListState) {
                        dispatchRawDelta((offset - firstVisibleItemScrollOffset).toFloat())
                    }
                }
                prevSize = newSize
            }
            .collect()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        // Save the current offset when the screen is paused
        offset = lazyListState.firstVisibleItemScrollOffset
    }
    return this then Modifier
        .onSizeChanged { newSize ->
            size = newSize
        }
}
