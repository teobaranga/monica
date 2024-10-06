package com.teobaranga.monica.journal.view.ui

import androidx.compose.material3.TextFieldDefaults.FocusedIndicatorThickness
import androidx.compose.material3.TextFieldDefaults.UnfocusedIndicatorThickness
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class StartVerticalLineShape(val isFocused: () -> Boolean) : Shape {

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
