package com.teobaranga.monica

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.teobaranga.monica.browser.PlatformWebBrowser

class AndroidWebBrowser(private val context: Context) : PlatformWebBrowser {

    override fun open(url: String) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        intent.launchUrl(context, url.toUri())
    }
}
