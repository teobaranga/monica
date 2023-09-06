package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.teobaranga.monica.setup.SetupScreen
import com.teobaranga.monica.setup.SetupViewModel
import com.teobaranga.monica.ui.theme.MonicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                val viewModel by viewModels<SetupViewModel>()
                val uiState = viewModel.uiState
                SetupScreen(
                    uiState = uiState,
                    onSignIn = viewModel::onSignIn,
                )
            }
        }
    }
}
