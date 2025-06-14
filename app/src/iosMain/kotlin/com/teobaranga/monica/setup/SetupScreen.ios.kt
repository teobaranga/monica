package com.teobaranga.monica.setup

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import monica.app.generated.resources.Res
import monica.app.generated.resources.eb_garamond_regular
import org.jetbrains.compose.resources.Font

@Composable
actual fun logoFontFamily() = FontFamily(
    Font(Res.font.eb_garamond_regular)
)
