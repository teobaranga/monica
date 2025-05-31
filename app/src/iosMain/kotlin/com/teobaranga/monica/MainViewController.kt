package com.teobaranga.monica

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.iosWebBrowser
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.di.createAppComponent
import com.teobaranga.monica.home.HomeRoute

val appComponent = createAppComponent()

val viewModelFactoryOwner = object : ViewModelFactoryOwner {
    override val viewModelFactory: ViewModelProvider.Factory
        get() = appComponent.viewModelFactory

}

fun MainViewController() = ComposeUIViewController {
    MonicaTheme(
        dynamicColor = false,
    ) {
        val navController = rememberNavController()
        CompositionLocalProvider(
            LocalViewModelFactoryOwner provides viewModelFactoryOwner,
            LocalNavigator provides navController,
            LocalWebBrowser provides requireNotNull(iosWebBrowser),
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
