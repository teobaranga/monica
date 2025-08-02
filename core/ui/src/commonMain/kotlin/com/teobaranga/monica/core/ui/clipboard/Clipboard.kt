package com.teobaranga.monica.core.ui.clipboard

import androidx.compose.ui.platform.Clipboard

suspend fun Clipboard.setPlainText(text: String) = setPlainText(text, text)

expect suspend fun Clipboard.setPlainText(label: String, text: String)
