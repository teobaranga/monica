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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme

@Composable
fun Account(
    viewModel: AccountViewModel = injectedViewModel(),
    onDismissRequest: () -> Unit,
) {
    val navigator = LocalNavigator.current
    AccountScreen(
        onClearAuthorization = viewModel::onClearAuthorization,
        onViewCertificates = {
            navigator.navigate(CertificateListRoute(CertificateTrustStatus.TRUSTED))
            onDismissRequest()
        },
        onDismissRequest = onDismissRequest,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    onClearAuthorization: () -> Unit,
    onViewCertificates: () -> Unit,
    onDismissRequest: () -> Unit,
) {
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
            Button(
                onClick = onViewCertificates,
            ) {
                Text(text = "Certificates")
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
                onViewCertificates = { },
                onDismissRequest = { },
            )
        }
    }
}
