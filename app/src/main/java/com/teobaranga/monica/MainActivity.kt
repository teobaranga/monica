package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.teobaranga.monica.setup.SetupScreen
import com.teobaranga.monica.setup.UiState
import com.teobaranga.monica.ui.theme.MonicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                val uiState by rememberSaveable(stateSaver = UiState.Saver) {
                    mutableStateOf(UiState())
                }
                SetupScreen(
                    uiState = uiState,
                    onSignIn = {

                    },
                )
            }
        }
    }
}
