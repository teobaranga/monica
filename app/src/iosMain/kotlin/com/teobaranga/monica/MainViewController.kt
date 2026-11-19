package com.teobaranga.monica

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.iosWebBrowser
import com.teobaranga.monica.di.AppComponent
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

val appComponent = createGraph<AppComponent>()

fun MainViewController() = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides appComponent.metroViewModelFactory,
        LocalWebBrowser provides requireNotNull(iosWebBrowser),
    ) {
        MonicaApp()
    }
}
