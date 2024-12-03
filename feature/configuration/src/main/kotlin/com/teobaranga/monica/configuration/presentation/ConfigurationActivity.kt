package com.teobaranga.monica.configuration.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.teobaranga.monica.inject.runtime.LocalViewModelFactoryOwner
import com.teobaranga.monica.inject.runtime.ViewModelFactoryOwner
import com.teobaranga.monica.ui.theme.MonicaTheme

class ConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                CompositionLocalProvider(
                    LocalViewModelFactoryOwner provides applicationContext as ViewModelFactoryOwner,
                ) {
                    ConfigurationScreen(
                        onClose = ::finish,
                    )
                }
            }
        }
    }
}
