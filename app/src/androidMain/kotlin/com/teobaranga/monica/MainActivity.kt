package com.teobaranga.monica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.util.Consumer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.home.HomeRoute
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

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
            MonicaTheme(
                dynamicColor = false,
            ) {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalViewModelFactoryOwner provides getViewModelFactoryOwner(),
                    LocalNavigator provides navController,
                    LocalWebBrowser provides AndroidWebBrowser(this),
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                        startDestination = HomeRoute,
                        builder = RootNavGraphBuilder,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOnNewIntentListener(intentListener)
    }

    private fun getViewModelFactoryOwner(): ViewModelFactoryOwner {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactoryOwner(AppScope::class)
    }
}
