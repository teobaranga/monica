package com.teobaranga.monica.util.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Parse a [Color] from a color hex string, such as #FF000080.
 */
fun Color.Companion.fromHex(hexColor: String): Color {
    return Color(hexColor.removePrefix("#").toLong(16) or 0x00000000FF000000)
}

/**
 * Determine if this is a light theme based on the background luminance value.
 */
@Composable
fun ColorScheme.isLight(): Boolean {
    return background.luminance() > 0.5
}
