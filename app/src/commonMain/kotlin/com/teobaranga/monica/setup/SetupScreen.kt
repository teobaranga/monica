package com.teobaranga.monica.setup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.data.PARAM_CODE
import com.teobaranga.monica.home.HomeRoute
import com.teobaranga.monica.util.compose.keyboardAsState
import kotlinx.coroutines.flow.collectLatest
import monica.app.generated.resources.Res
import monica.app.generated.resources.monica
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SETUP_INFO_URL = "https://monica.teobaranga.com/setup"

@Composable
expect fun logoFontFamily(): FontFamily

@Composable
fun Setup(
    viewModel: SetupViewModel = injectedViewModel<SetupViewModel, SetupViewModel.Factory>(
        creationCallback = { factory ->
            factory(createSavedStateHandle())
        }
    ),
) {
    val webBrowser = LocalWebBrowser.current
    val navigator = LocalNavigator.current
    val uiState = viewModel.uiState

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navigator.navigate(HomeRoute) {
                popUpTo(SetupRoute) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setupUri
            .collectLatest { url ->
                webBrowser.open(url)
            }
    }

    LaunchedEffect(Unit) {
        AppLinksHandler.url
            .collectLatest { url ->
                url.parameters[PARAM_CODE]?.let { code ->
                    viewModel.onAuthorizationCode(code)
                }
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
            painter = painterResource(Res.drawable.monica),
            contentDescription = null,
        )

        Text(
            text = "MONICA",
            style = MaterialTheme.typography.displayLarge,
            fontFamily = logoFontFamily(),
        )
    }
}

@Composable
private fun SetupSectionTitle(modifier: Modifier = Modifier) {
    val webBrowser = LocalWebBrowser.current
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

        IconButton(
            onClick = {
                webBrowser.open(SETUP_INFO_URL)
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "OAuth 2.0 setup information",
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSetupScreen() {
    MonicaTheme {
        SetupScreen(
            uiState = UiState(),
            onSignIn = { },
        )
    }
}
