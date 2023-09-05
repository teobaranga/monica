package com.teobaranga.monica.setup

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.R
import com.teobaranga.monica.ui.theme.MonicaTheme

@Composable
fun SetupScreen(
    uiState: UiState,
    onSignIn: () -> Unit,
) {
    MonicaBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .imePadding(),
        ) {
            Image(
                modifier = Modifier
                    .widthIn(max = 192.dp)
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.monica),
                contentDescription = null,
            )
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "OAuth 2.0 Setup",
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 20.dp),
                value = uiState.serverAddress,
                onValueChange = {
                    uiState.serverAddress = it
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
                    uiState.clientId = it
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
                    uiState.clientSecret = it
                },
                label = {
                    Text(text = "Client Secret")
                },
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 20.dp),
                onClick = onSignIn,
                content = {
                    Text(
                        text = "Sign In"
                    )
                }
            )
        }
    }
}

@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
)
@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PreviewSetupScreen() {
    MonicaTheme {
        SetupScreen(
            uiState = UiState(),
            onSignIn = { },
        )
    }
}
