package com.teobaranga.monica.setup

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.SetupDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.R
import com.teobaranga.monica.data.PARAM_CODE
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.keyboardAsState
import kotlinx.coroutines.flow.collectLatest

private const val SETUP_INFO_URL = "https://github.com/teobaranga/monica?tab=readme-ov-file#setup"

private val logoFontFamily = FontFamily(Font(R.font.eb_garamond_variable))

@Destination<RootGraph>
@Composable
fun Setup(
    navigator: DestinationsNavigator,
    viewModel: SetupViewModel = injectedViewModel(),
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navigator.navigate(NavGraphs.root) {
                popUpTo(SetupDestination) {
                    inclusive = true
                }
            }
        }
    }

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
        val componentActivity = context as ComponentActivity
        val listener = Consumer<Intent> { intent ->
            val code = intent.data?.getQueryParameter(PARAM_CODE)
            viewModel.onAuthorizationCode(code)
        }
        componentActivity.addOnNewIntentListener(listener)
        onDispose {
            componentActivity.removeOnNewIntentListener(listener)
        }
    }

    SetupScreen(
        uiState = uiState,
        onSignIn = viewModel::onSignIn,
    )
}

@Composable
fun SetupScreen(uiState: UiState, onSignIn: () -> Unit, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    // Scroll to bottom to keep the main button visible when the IME is open
    val keyboardState by keyboardAsState()
    LaunchedEffect(keyboardState) {
        if (keyboardState) {
            scrollState.animateScrollTo(Int.MAX_VALUE)
        }
    }

    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .systemBarsPadding()
                .imePadding(),
        ) {
            Logo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            )
            SetupSectionTitle(
                modifier = Modifier
                    .padding(start = 20.dp, end = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 20.dp),
                value = uiState.serverAddress,
                onValueChange = {
                    uiState.onServerAddressChanged(it)
                },
                singleLine = true,
                label = {
                    Text(text = "Server Address")
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 20.dp),
                value = uiState.clientId,
                onValueChange = {
                    uiState.onClientIdChanged(it)
                },
                singleLine = true,
                label = {
                    Text(text = "Client ID")
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 20.dp),
                value = uiState.clientSecret,
                onValueChange = {
                    uiState.onClientSecretChanged(it)
                },
                label = {
                    Text(text = "Client Secret")
                },
            )
            uiState.error?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp),
                    text = when (it) {
                        UiState.Error.ConfigurationError -> "Please check your configuration"
                    },
                    color = Color.Red,
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 20.dp),
                onClick = onSignIn,
                enabled = uiState.isSignInEnabled,
                content = {
                    Text(
                        text = "Sign In",
                    )
                },
            )
        }
    }
}

@Composable
private fun Logo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .widthIn(max = 192.dp)
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.monica),
            contentDescription = null,
        )

        Text(
            text = "MONICA",
            style = MaterialTheme.typography.displayLarge,
            fontFamily = logoFontFamily,
        )
    }
}

@Composable
private fun SetupSectionTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = "OAuth 2.0 Setup",
            style = MaterialTheme.typography.labelLarge,
        )

        SetupInfoButton()
    }
}

@Composable
private fun SetupInfoButton() {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            intent.launchUrl(context, Uri.parse(SETUP_INFO_URL))
        },
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "OAuth 2.0 setup information",
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewSetupScreen() {
    MonicaTheme {
        SetupScreen(
            uiState = UiState(),
            onSignIn = { },
        )
    }
}
