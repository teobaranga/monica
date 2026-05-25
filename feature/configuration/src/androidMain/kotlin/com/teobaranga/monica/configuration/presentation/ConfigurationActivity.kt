package com.teobaranga.monica.configuration.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

class ConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                CompositionLocalProvider(
                    LocalMetroViewModelFactory provides getViewModelFactoryOwner(),
                ) {
                    ConfigurationScreen(
                        onClose = ::finish,
                    )
                }
            }
        }
    }

    private fun getViewModelFactoryOwner(): MetroViewModelFactory {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactory(AppScope::class)
    }
}
