package com.teobaranga.monica.util.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * The strategy used to determine if the cursor is at risk of being behind the keyboard.
 * This logic can be screen-dependent, taking into account different UI elements and the position of the TextField.
 */
fun interface CursorVisibilityStrategy {

    /**
     * Determines if the cursor is at risk of being behind the keyboard after it scrolls up fully.
     * For example, one strategy can return `true` if the cursor is in the bottom half of the text field.
     * Returning `true` will kickstart the process that moves the keyboard height as it grows into the scroll state.
     *
     * @param cursor The cursor's bounds in the text field.
     * @param boundsInWindow The bounds of the text field in the window.
     * @param screenHeight The height of the screen.
     * @param scrollState The scroll state of the text field or its container.
     */
    fun Density.isCursorAtRisk(cursor: Rect, boundsInWindow: Rect, screenHeight: Int, scrollState: ScrollState): Boolean
}

/**
 * Holds the data required for the [keepCursorVisible] Modifier.
 *
 * The [scrollState] and [textLayoutResult] are meant to be passed to the `TextField` for which
 * the cursor should be kept visible.
 */
sealed interface CursorData {

    val scrollState: ScrollState

    val textLayoutResult: (Density.(getResult: () -> TextLayoutResult?) -> Unit)

    /**
     * The [CursorVisibilityStrategy] to use to determine if the cursor is at risk of being behind the keyboard.
     */
    val cursorVisibilityStrategy: CursorVisibilityStrategy

    var cursorRect: Rect?
}

private class CursorDataImpl(
    private val textFieldState: TextFieldState,
    override val cursorVisibilityStrategy: CursorVisibilityStrategy,
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
fun rememberCursorData(
    textFieldState: TextFieldState,
    cursorVisibilityStrategy: CursorVisibilityStrategy,
    scrollState: ScrollState = rememberScrollState(),
): CursorData {
    return remember(textFieldState, cursorVisibilityStrategy, scrollState) {
        CursorDataImpl(textFieldState, cursorVisibilityStrategy, scrollState)
    }
}

/**
 * Modifier that keeps the cursor visible for a text field that's into focus and as the keyboard is
 * displayed. Without this, the keyboard can draw on top of the TextField, leaving the cursor offscreen.
 *
 * The keyboard height is observed, and as it comes into view, its height is shifted into the scroll state
 * in order to keep the cursor visible.
 */
@Composable
fun Modifier.keepCursorVisible(cursorData: CursorData): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.roundToPx()
    }
    val keyboardHeight by rememberUpdatedState(WindowInsets.ime.getBottom(density))
    var boundsInWindow by remember { mutableStateOf(Rect.Zero) }
    var prevKeyboardHeight by remember { mutableIntStateOf(0) }
    var isKeyboardGrowing by remember { mutableStateOf(false) }
    var isInProgress by remember { mutableStateOf(false) }
    val shouldScroll = {
        // Only scroll if the TextField is focused and the cursor is at risk, as determined by the current strategy.
        // Any incoming increases in keyboard height will be shifted into the scroll value to keep the cursor visible.
        val cursor = cursorData.cursorRect
        if (isFocused && cursor != null) {
            val isCursorAtRisk = cursorData.cursorVisibilityStrategy.run {
                with(density) {
                    isCursorAtRisk(cursor, boundsInWindow, screenHeight, cursorData.scrollState)
                }
            }
            // As the TextField is scrolled, eventually the cursor might be visible before the keyboard
            // fully animates up. In that case, shouldScroll would become false and the scrolling would stop
            // abruptly. To prevent this, we keep scrolling if a scroll is already in progress.
            isCursorAtRisk || isInProgress
        } else {
            false
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { keyboardHeight }
            .collect { newKeyboardHeight ->
                isKeyboardGrowing = newKeyboardHeight > prevKeyboardHeight

                // Only scroll if deemed necessary and the keyboard is coming into view. Scrolling as the keyboard is
                // going out could actually place the cursor outside of the screen if it's at the bottom of the field.
                if (shouldScroll() && isKeyboardGrowing) {
                    val delta = (newKeyboardHeight - prevKeyboardHeight).toFloat()
                    cursorData.scrollState.dispatchRawDelta(delta)
                    isInProgress = true
                } else {
                    isInProgress = false
                }

                prevKeyboardHeight = newKeyboardHeight
            }
    }
    return this then Modifier
        .onFocusChanged {
            isFocused = it.isFocused
        }
        .onGloballyPositioned { layoutCoordinates ->
            boundsInWindow = layoutCoordinates.boundsInWindow()
        }
}
