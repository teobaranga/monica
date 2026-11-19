package com.teobaranga.monica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.util.Consumer
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

class MainActivity : ComponentActivity() {

    private val intentListener = Consumer<Intent> { intent ->
        intent.data?.let {
            AppLinksHandler.handle(it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        addOnNewIntentListener(intentListener)

        setContent {
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides getViewModelFactory(),
                LocalWebBrowser provides AndroidWebBrowser(this),
            ) {
                MonicaApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOnNewIntentListener(intentListener)
    }

    private fun getViewModelFactory(): MetroViewModelFactory {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactory(AppScope::class)
    }
}
