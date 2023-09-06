package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teobaranga.monica.setup.SetupScreen
import com.teobaranga.monica.setup.SetupViewModel
import com.teobaranga.monica.ui.theme.MonicaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                val context = LocalContext.current
                val viewModel = viewModel<SetupViewModel>()
                val uiState = viewModel.uiState

                LaunchedEffect(Unit) {
                    viewModel.setupUri
                        .collectLatest { url ->
                            val intent = CustomTabsIntent.Builder()
                                .setShowTitle(true)
                                .build()
                            intent.launchUrl(context, url.toUri())
                        }
                }

                SetupScreen(
                    uiState = uiState,
                    onSignIn = viewModel::onSignIn,
                )
            }
        }
    }
}
