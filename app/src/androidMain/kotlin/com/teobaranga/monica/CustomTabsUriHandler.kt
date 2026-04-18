package com.teobaranga.monica

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.UriHandler
import androidx.core.net.toUri

class CustomTabsUriHandler(private val context: Context) : UriHandler {

    override fun openUri(uri: String) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        intent.launchUrl(context, uri.toUri())
    }
}
