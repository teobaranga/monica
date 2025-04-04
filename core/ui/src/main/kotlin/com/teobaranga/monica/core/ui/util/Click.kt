package com.teobaranga.monica.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Debounce an action with the given [debounceInterval].
 * Useful when preventing multiple clicks on a button.
 */
@Composable
inline fun debounce(
    debounceInterval: Duration = 400.milliseconds,
    crossinline action: () -> Unit,
): () -> Unit {
    var lastClickTime = remember<TimeMark?> { null }
    return {
        val elapsed = lastClickTime?.elapsedNow()
        if (elapsed == null || elapsed > debounceInterval) {
            lastClickTime = TimeSource.Monotonic.markNow()
            action()
        }
    }
}
