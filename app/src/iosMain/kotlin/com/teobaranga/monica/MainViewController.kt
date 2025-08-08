package com.teobaranga.monica

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.iosWebBrowser
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.di.createAppComponent

val appComponent = createAppComponent()

val viewModelFactoryOwner = object : ViewModelFactoryOwner {
    override val viewModelFactory: ViewModelProvider.Factory
        get() = appComponent.viewModelFactory

}

fun MainViewController() = ComposeUIViewController {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalViewModelFactoryOwner provides viewModelFactoryOwner,
        LocalNavigator provides navController,
        LocalWebBrowser provides requireNotNull(iosWebBrowser),
    ) {
        MonicaApp(
            navController = navController,
        )
    }
}
