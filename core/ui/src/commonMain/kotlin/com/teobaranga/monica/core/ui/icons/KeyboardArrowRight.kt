package com.teobaranga.monica.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val KeyboardArrowRight: ImageVector
    get() {
        if (_KeyboardArrowRight != null) return _KeyboardArrowRight!!

        _KeyboardArrowRight = ImageVector.Builder(
            name = "Keyboard_arrow_right",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(504f, 480f)
                lineTo(320f, 296f)
                lineToRelative(56f, -56f)
                lineToRelative(240f, 240f)
                lineToRelative(-240f, 240f)
                lineToRelative(-56f, -56f)
                close()
            }
        }.build()

        return _KeyboardArrowRight!!
    }

private var _KeyboardArrowRight: ImageVector? = null
