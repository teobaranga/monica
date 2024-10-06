package com.teobaranga.monica.util.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density

/**
 * Holds the data required for the [keepCursorVisible] Modifier.
 *
 * The [scrollState] and [textLayoutResult] are meant to be passed to the `TextField` for which
 * the cursor should be kept visible.
 */
interface CursorData {

    val scrollState: ScrollState

    val textLayoutResult: (Density.(getResult: () -> TextLayoutResult?) -> Unit)

    var cursorRect: Rect?
}

@Stable
private class CursorDataImpl(
    private val textFieldState: TextFieldState,
    override val scrollState: ScrollState,
) : CursorData {

    override var cursorRect by mutableStateOf<Rect?>(null)

    override val textLayoutResult: Density.(getResult: () -> TextLayoutResult?) -> Unit = { getResult ->
        cursorRect = getResult()?.getCursorRect(textFieldState.selection.start)
    }
}

/**
 * Remember a [CursorData] instance.
 *
 * @param textFieldState The [TextFieldState] for which the cursor should be kept visible.
 * @param scrollState The [ScrollState] for the same `TextField`.
 */
@Composable
fun rememberCursorData(textFieldState: TextFieldState, scrollState: ScrollState = rememberScrollState()): CursorData {
    return remember(textFieldState, scrollState) {
        CursorDataImpl(textFieldState, scrollState)
    }
}

/**
 * Modifier that keeps the cursor visible as a text field comes into focus and the keyboard is
 * displayed, shrinking the size. Without this, the text field would shrink without adjust the scroll
 * state, leaving the cursor out of view.
 */
@Composable
fun Modifier.keepCursorVisible(cursorData: CursorData): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    var height by remember { mutableIntStateOf(0) }
    var prevScrollMaxValue by remember { mutableIntStateOf(cursorData.scrollState.maxValue) }
    var isShrinking by remember { mutableStateOf(false) }
    var isInProgress by remember { mutableStateOf(false) }
    val shouldScroll by remember(isFocused) {
        derivedStateOf {
            // Only scroll if the TextField is focused and the cursor location is in the bottom
            // half of the TextField. It's highly likely that this area would be hidden by the keyboard
            // when it opens up. Therefore, any incoming increases in max scroll value should be shifted into
            // the scroll value to keep the cursor visible.
            val cursor = cursorData.cursorRect
            if (isFocused && cursor != null) {
                val isCursorInBottomHalf = cursor.bottom - cursorData.scrollState.value > height / 2
                // As the TextField is scrolled, eventually the cursor might be visible before the keyboard
                // fully animates up. In that case, shouldScroll would become false and the scrolling would stop
                // abruptly. To prevent this, we keep scrolling if a scroll is already in progress.
                isCursorInBottomHalf || isInProgress
            } else {
                false
            }
        }
    }

    LaunchedEffect(Unit) {
        // The max value of the scroll state is updated along with the keyboard state. It increases
        // as the TextField shrinks and the keyboard is shown.
        snapshotFlow { cursorData.scrollState.maxValue }
            .collect { maxValue ->
                isShrinking = maxValue > prevScrollMaxValue

                // Only scroll if deemed necessary and the TextField is shrinking. Scrolling as the TextField is
                // growing could actually place the cursor outside of the screen if it's at the bottom of the field.
                if (shouldScroll && isShrinking) {
                    cursorData.scrollState.dispatchRawDelta((maxValue - prevScrollMaxValue).toFloat())
                    isInProgress = true
                } else {
                    isInProgress = false
                }

                prevScrollMaxValue = maxValue
            }
    }
    return this then Modifier
        .onFocusChanged {
            isFocused = it.isFocused
        }
        .onGloballyPositioned {
            height = it.size.height
        }
}
