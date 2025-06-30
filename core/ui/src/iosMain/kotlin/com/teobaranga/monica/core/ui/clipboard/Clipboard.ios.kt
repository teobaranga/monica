package com.teobaranga.monica.core.ui.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun Clipboard.setPlainText(label: String, text: String) {
    setClipEntry(ClipEntry.withPlainText(text))
}
