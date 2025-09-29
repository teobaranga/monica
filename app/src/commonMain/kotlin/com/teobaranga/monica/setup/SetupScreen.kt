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
import androidx.compose.foundation.text.input.TextFieldLineLimits
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.PreviewPlatformWebBrowser
import com.teobaranga.monica.core.ui.icon.MonicaLogo
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.data.PARAM_CODE
import com.teobaranga.monica.home.HomeRoute
import com.teobaranga.monica.util.compose.keyboardAsState
import kotlinx.coroutines.flow.collectLatest
import monica.app.generated.resources.Res
import monica.app.generated.resources.eb_garamond_variable
import monica.app.generated.resources.setup_client_id_label
import monica.app.generated.resources.setup_client_secret_label
import monica.app.generated.resources.setup_error_address_invalid
import monica.app.generated.resources.setup_error_address_protocol
import monica.app.generated.resources.setup_error_configuration_generic
import monica.app.generated.resources.setup_oauth
import monica.app.generated.resources.setup_oauth_content_description
import monica.app.generated.resources.setup_server_address_label
import monica.app.generated.resources.setup_sign_in
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SETUP_INFO_URL = "https://monica.teobaranga.com/setup"

@Composable
fun Setup(
    viewModel: SetupViewModel = injectedViewModel(),
) {
    val webBrowser = LocalWebBrowser.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    var insecureSetupUrl by remember { mutableStateOf<String?>(null) }

    RedirectEffect(isLoggedIn)

    LaunchedEffect(Unit) {
        viewModel.setupEvents
            .collectLatest { url ->
                when (url) {
                    is SetupEvent.Login -> {
                        if (url.isSecure) {
                            webBrowser.open(url.setupUrl)
                        } else {
                            insecureSetupUrl = url.setupUrl
                        }
                    }
                }
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

    insecureSetupUrl?.let {
        InsecureHttpBottomSheet(
            onDismiss = {
                insecureSetupUrl = null
            },
            onAccept = {
                insecureSetupUrl = null
                webBrowser.open(it)
            },
        )
    }
}

@Composable
private fun RedirectEffect(isLoggedIn: Boolean?) {
    val navigator = LocalNavigator.current
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navigator.navigate(HomeRoute) {
                popUpTo(SetupRoute) {
                    inclusive = true
                }
            }
        }
    }
}

@Composable
fun SetupScreen(
    uiState: UiState,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val error = uiState.error

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
                state = uiState.serverAddress,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text(text = stringResource(Res.string.setup_server_address_label))
                },
                isError = error is UiState.Error.ServerAddressProtocolError
                    || error is UiState.Error.ServerAddressInvalidError,
                supportingText = {
                    if (error is UiState.Error.ServerAddressProtocolError) {
                        Text(text = stringResource(Res.string.setup_error_address_protocol))
                    } else if (error is UiState.Error.ServerAddressInvalidError) {
                        Text(text = stringResource(Res.string.setup_error_address_invalid))
                    }
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 20.dp),
                state = uiState.clientId,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text(text = stringResource(Res.string.setup_client_id_label))
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 20.dp),
                state = uiState.clientSecret,
                label = {
                    Text(text = stringResource(Res.string.setup_client_secret_label))
                },
            )
            if (error is UiState.Error.ConfigurationError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp),
                    text = error.message
                        ?: stringResource(Res.string.setup_error_configuration_generic),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 20.dp),
                onClick = {
                    focusManager.clearFocus()
                    onSignIn()
                },
                enabled = uiState.isSignInEnabled,
                content = {
                    Text(
                        text = stringResource(Res.string.setup_sign_in),
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
            imageVector = MonicaLogo,
            contentDescription = null,
        )

        Text(
            text = "MONICA",
            style = MaterialTheme.typography.displayLarge,
            fontFamily = FontFamily(Font(Res.font.eb_garamond_variable)),
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
            text = stringResource(Res.string.setup_oauth),
            style = MaterialTheme.typography.labelLarge,
        )

        IconButton(
            onClick = {
                webBrowser.open(SETUP_INFO_URL)
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(Res.string.setup_oauth_content_description),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSetupScreen() {
    MonicaTheme {
        CompositionLocalProvider(
            LocalWebBrowser provides PreviewPlatformWebBrowser,
        ) {
            SetupScreen(
                uiState = UiState(),
                onSignIn = { },
            )
        }
    }
}
