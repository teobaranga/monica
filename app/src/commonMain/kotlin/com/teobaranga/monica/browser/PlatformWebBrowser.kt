package com.teobaranga.monica.browser

import androidx.compose.runtime.staticCompositionLocalOf

val LocalWebBrowser = staticCompositionLocalOf<PlatformWebBrowser> { error("No WebBrowser provided") }

interface PlatformWebBrowser {

    fun open(url: String)
}
