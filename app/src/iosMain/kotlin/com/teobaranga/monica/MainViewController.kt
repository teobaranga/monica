package com.teobaranga.monica

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.squareup.metro.createGraph
import com.squareup.metro.metrox.viewmodel.compose.LocalMetroViewModelFactory
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.iosWebBrowser
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.di.AppComponent

val appComponent = createGraph<AppComponent>()

fun MainViewController() = ComposeUIViewController {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides appComponent.metroViewModelFactory,
        LocalNavigator provides navController,
        LocalWebBrowser provides requireNotNull(iosWebBrowser),
    ) {
        MonicaApp(
            navController = navController,
        )
    }
}
