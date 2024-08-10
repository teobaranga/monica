package com.teobaranga.monica.configuration.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teobaranga.monica.ui.theme.MonicaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                ConfigurationScreen(
                    onClose = ::finish,
                )
            }
        }
    }
}
