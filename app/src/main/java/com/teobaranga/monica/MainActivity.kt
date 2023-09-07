package com.teobaranga.monica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teobaranga.monica.data.PARAM_CODE
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

                DisposableEffect(Unit) {
                    val listener = Consumer<Intent> { intent ->
                        val code = intent.data?.getQueryParameter(PARAM_CODE)
                        viewModel.onAuthorizationCode(code)
                    }
                    addOnNewIntentListener(listener)
                    onDispose { removeOnNewIntentListener(listener) }
                }

                SetupScreen(
                    uiState = uiState,
                    onSignIn = viewModel::onSignIn,
                )
            }
        }
    }
}
