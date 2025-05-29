package com.teobaranga.monica.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Account(
    viewModel: AccountViewModel = injectedViewModel(),
    onDismissRequest: () -> Unit,
) {
    AccountScreen(
        onClearAuthorization = viewModel::onClearAuthorization,
        onDismissRequest = onDismissRequest,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(onClearAuthorization: () -> Unit, onDismissRequest: () -> Unit) {
    ModalBottomSheet(
        modifier = Modifier
            .statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        contentWindowInsets = { WindowInsets.Zero },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(ShapeDefaults.Large)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Button(
                onClick = onClearAuthorization,
            ) {
                Text(text = "Sign out")
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Preview
@Composable
private fun PreviewAccountScreen() {
    MonicaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AccountScreen(
                onClearAuthorization = { },
                onDismissRequest = { },
            )
        }
    }
}
