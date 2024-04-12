package com.teobaranga.monica.util.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

/**
 * Modifier useful for nested lazy lists where the parent (the list associated with the [state]) should scroll
 * before any children.
 */
fun Modifier.nestedScrollParentFirst(state: LazyListState): Modifier {
    return this
        .composed {
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        if (available.y < 0) {
                            val consumed = state.dispatchRawDelta(-available.y)
                            return Offset.Zero.copy(y = -consumed)
                        } else {
                            return Offset.Zero
                        }
                    }
                }
            }
            nestedScroll(nestedScrollConnection)
        }
}
