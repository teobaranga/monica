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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Holds the data required for the [keepCursorVisible] Modifier.
 *
 * The [scrollState] and [textLayoutResult] are meant to be passed to the `TextField` for which
 * the cursor should be kept visible.
 */
sealed interface CursorData {

    val scrollState: ScrollState

    val textLayoutResult: (Density.(getResult: () -> TextLayoutResult?) -> Unit)

    var cursorRect: Rect?
}

sealed class CursorDataBase(
) : CursorData {

    abstract val textFieldState: TextFieldState

    override var cursorRect by mutableStateOf<Rect?>(null)

    override val textLayoutResult: Density.(getResult: () -> TextLayoutResult?) -> Unit = { getResult ->
        cursorRect = getResult()?.getCursorRect(textFieldState.selection.start)
    }
}

@Stable
private class FixedContainerCursorData(
    override val textFieldState: TextFieldState,
    override val scrollState: ScrollState,
) : CursorDataBase() {

    fun isCursorInBottomHalf(cursor: Rect, height: Int): Boolean {
        return cursor.top - cursor.height - scrollState.value > height / 2
    }
}

/**
 * @param textFieldState The [TextFieldState] for which the cursor should be kept visible.
 * @param scrollState The [ScrollState] for the `TextField` container.
 */
@Stable
private class ScrollableContainerCursorData(
    override val textFieldState: TextFieldState,
    override val scrollState: ScrollState,
) : CursorDataBase() {

    fun isCursorInBottomHalf(cursor: Rect, positionInRoot: Offset, screenHeight: Int): Boolean {
        return positionInRoot.y + cursor.top - cursor.height - scrollState.value.coerceAtMost(scrollState.maxValue) > screenHeight / 2
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
    isFixedContainer: Boolean,
    scrollState: ScrollState = rememberScrollState(),
): CursorData {
    return remember(textFieldState, isFixedContainer, scrollState) {
        if (isFixedContainer) {
            FixedContainerCursorData(textFieldState, scrollState)
        } else {
            ScrollableContainerCursorData(textFieldState, scrollState)
        }
    }
}

/**
 * Modifier that keeps the cursor visible as a text field comes into focus and the keyboard is
 * displayed, shrinking the size. Without this, the text field would shrink without adjust the scroll
 * state, leaving the cursor out of view.
 */
@Composable
fun Modifier.keepCursorVisible(cursorData: CursorData): Modifier {
    var positionInRoot by remember { mutableStateOf(Offset.Zero) }
    var isFocused by remember { mutableStateOf(false) }
    var height by remember { mutableIntStateOf(0) }
    var rootHeight by remember { mutableIntStateOf(0) }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.roundToPx()
    }
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
                val isCursorInBottomHalf = when (cursorData) {
                    is FixedContainerCursorData -> cursorData.isCursorInBottomHalf(cursor, height)
                    is ScrollableContainerCursorData -> cursorData.isCursorInBottomHalf(
                        cursor,
                        positionInRoot,
                        screenHeight,
                    )
                }
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
        .onGloballyPositioned { layoutCoordinates ->
            positionInRoot = layoutCoordinates.positionInRoot()
            rootHeight = layoutCoordinates.findRootCoordinates().size.height
            height = layoutCoordinates.size.height
        }
}
