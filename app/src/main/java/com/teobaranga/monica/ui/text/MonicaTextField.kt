package com.teobaranga.monica.ui.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

// Developers need to handle invalid input manually. But since we don't provide an error message
// slot API, we can set the default error message in case developers forget about it.
private fun Modifier.defaultErrorSemantics(isError: Boolean, defaultErrorMessage: String): Modifier =
    if (isError) semantics { error(defaultErrorMessage) } else this

/**
 * Represents the color used for the input field of this text field.
 *
 * @param enabled whether the text field is enabled
 * @param isError whether the text field's current value is in error
 * @param focused whether the text field is in focus
 */
@Stable
private fun TextFieldColors.textColor(enabled: Boolean, isError: Boolean, focused: Boolean): Color = when {
    !enabled -> disabledTextColor
    isError -> errorTextColor
    focused -> focusedTextColor
    else -> unfocusedTextColor
}

/**
 * Represents the color used for the cursor of this text field.
 *
 * @param isError whether the text field's current value is in error
 */
@Stable
private fun TextFieldColors.cursorColor(isError: Boolean): Color = if (isError) errorCursorColor else cursorColor

/**
 * Clone of the Material 3 `OutlinedTextField` with extra functionality:
 *
 * - `onTextLayout`
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        val focused = interactionSource.collectIsFocusedAsState().value
        colors.textColor(enabled, isError, focused)
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            state = state,
            modifier = if (label != null) {
                modifier
                    // Merge semantics at the beginning of the modifier chain to ensure padding is
                    // considered part of the text field.
                    .semantics(mergeDescendants = true) {}
                    .padding(top = OutlinedTextFieldTopPadding)
            } else {
                modifier
            }
                .defaultErrorSemantics(isError, stringResource(androidx.compose.ui.R.string.default_error_message))
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight,
                ),
            enabled = enabled,
            readOnly = readOnly,
            inputTransformation = inputTransformation,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            outputTransformation = outputTransformation,
            lineLimits = lineLimits,
            onTextLayout = onTextLayout,
            scrollState = scrollState,
            decorator = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = state.text.toString(),
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    singleLine = lineLimits is TextFieldLineLimits.SingleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = contentPadding,
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape,
                        )
                    },
                )
            },
        )
    }
}

/*
This padding is used to allow label not overlap with the content above it. This 8.dp will work
for default cases when developers do not override the label's font size. If they do, they will
need to add additional padding themselves
*/
/* @VisibleForTesting */
private val OutlinedTextFieldTopPadding = 8.dp
