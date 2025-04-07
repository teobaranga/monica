package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.home.HomeRoute
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalViewModelFactoryOwner provides getViewModelFactoryOwner(),
                    LocalNavigator provides navController,
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

    private fun getViewModelFactoryOwner(): ViewModelFactoryOwner {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactoryOwner(AppScope::class)
    }
}
