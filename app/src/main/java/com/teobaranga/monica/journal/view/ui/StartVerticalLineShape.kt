package com.teobaranga.monica.journal.view.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val lineWidth = 1.dp

object StartVerticalLineShape : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            addRect(
                Rect(
                    topLeft = Offset(0f, 0f),
                    bottomRight = Offset(with(density) { lineWidth.toPx() }, size.height),
                ),
            )
        }
        return Outline.Generic(path)
    }
}
