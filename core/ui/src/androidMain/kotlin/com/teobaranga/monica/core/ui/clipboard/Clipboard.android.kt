package com.teobaranga.monica.core.ui.clipboard

import android.content.ClipData
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry

actual suspend fun Clipboard.setPlainText(label: String, text: String) {
    setClipEntry(ClipData.newPlainText(label, text).toClipEntry())
}
