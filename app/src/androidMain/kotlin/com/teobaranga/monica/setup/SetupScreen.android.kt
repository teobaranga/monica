package com.teobaranga.monica.setup

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import monica.app.generated.resources.Res
import monica.app.generated.resources.eb_garamond_regular
import monica.app.generated.resources.eb_garamond_variable
import org.jetbrains.compose.resources.Font

@Composable
actual fun logoFontFamily() = FontFamily(
    Font(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Res.font.eb_garamond_variable
        } else {
            Res.font.eb_garamond_regular
        },
    )
)
