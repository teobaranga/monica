package com.teobaranga.monica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
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
            val navController = rememberNavController()
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides getViewModelFactory(),
                LocalNavigator provides navController,
                LocalUriHandler provides CustomTabsUriHandler(this),
            ) {
                MonicaApp(
                    navController = navController,
                )
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
