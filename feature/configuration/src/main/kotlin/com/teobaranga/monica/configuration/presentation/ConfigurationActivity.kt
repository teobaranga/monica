package com.teobaranga.monica.configuration.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.ui.theme.MonicaTheme
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

class ConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                CompositionLocalProvider(
                    LocalViewModelFactoryOwner provides getViewModelFactoryOwner(),
                ) {
                    ConfigurationScreen(
                        onClose = ::finish,
                    )
                }
            }
        }
    }

    private fun getViewModelFactoryOwner(): ViewModelFactoryOwner {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactoryOwner(AppScope::class)
    }
}
