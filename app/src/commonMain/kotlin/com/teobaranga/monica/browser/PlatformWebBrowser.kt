package com.teobaranga.monica.browser

import androidx.compose.runtime.staticCompositionLocalOf

val LocalWebBrowser = staticCompositionLocalOf<PlatformWebBrowser> { error("No WebBrowser provided") }

interface PlatformWebBrowser {

    fun open(url: String)
}

/**
 * Only used for previews.
 */
object PreviewPlatformWebBrowser : PlatformWebBrowser {
    override fun open(url: String) {
        // Do nothing
    }
}
