package com.teobaranga.monica.core.ui.text

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.TextFieldDefaults.FocusedIndicatorThickness
import androidx.compose.material3.TextFieldDefaults.UnfocusedIndicatorThickness
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

private class StartVerticalLineShape(val isFocused: () -> Boolean) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(
            Path().apply {
                val width = with(density) {
                    if (isFocused()) {
                        FocusedIndicatorThickness.toPx()
                    } else {
                        UnfocusedIndicatorThickness.toPx()
                    }
                }
                moveTo(0f, 0f)
                lineTo(0f, size.height)
                lineTo(width, size.height)
                lineTo(width, 0f)
                close()
            },
        )
    }
}

@Composable
fun startVerticalLineShape(interactionSource: InteractionSource): Shape {
    val isFocused by interactionSource.collectIsFocusedAsState()
    return StartVerticalLineShape(isFocused = { isFocused })
}
