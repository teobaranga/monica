package com.teobaranga.monica.certificate.popup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.map

private val textButtonMinWidth = 64.dp

@Composable
fun UntrustedCertificatePopup(navHostController: NavHostController) {
    val viewModel = injectedViewModel<CertificateIssueViewModel>()
    val hasUntrustedCertificates by viewModel.hasUntrustedCertificates.collectAsStateWithLifecycle()
    val isViewingDetails by navHostController.currentBackStackEntryFlow
        .map {
            it.destination.hasRoute(CertificateListRoute::class)
                || it.destination.hasRoute(CertificateDetailsRoute::class)
        }
        .collectAsStateWithLifecycle(false)
    val shouldShow by remember {
        derivedStateOf {
            hasUntrustedCertificates && !isViewingDetails
        }
    }
    if (shouldShow) {
        CertificateIssueBottomSheet(
            onDismissRequest = viewModel::onDismiss,
            onViewDetails = {
                navHostController.navigate(CertificateListRoute(CertificateTrustStatus.UNTRUSTED))
            },
            onReject = viewModel::onDismiss,
            onAccept = viewModel::onTrust,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CertificateIssueBottomSheet(
    onDismissRequest: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onViewDetails: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text(
                text = "The identity of the server could not be verified",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = """
                    - The server certificate is not trusted
                    - The server certificate expired
                    - The server certificate valid dates are in the future
                    - The URL does not match the hostname in the certificate
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Do you want to trust this certificate anyway?",
                style = MaterialTheme.typography.bodyMedium,
            )
            ActionButtonRow(
                onAccept = onAccept,
                onReject = onReject,
                onViewDetails = onViewDetails,
            )
        }
    }
}

@Composable
private fun ActionButtonRow(
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onViewDetails: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp),
    ) {
        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = textButtonMinWidth),
            onClick = onViewDetails,
        ) {
            Text("Details")
        }
        Spacer(
            modifier = Modifier.weight(1f),
        )
        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = textButtonMinWidth),
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            onClick = onReject,
        ) {
            Text("No")
        }
        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = textButtonMinWidth),
            onClick = onAccept,
        ) {
            Text("Yes")
        }
    }
}

@Preview
@Composable
private fun UntrustedCertificateBottomSheetPreview() {
    MonicaTheme {
        CertificateIssueBottomSheet(
            onDismissRequest = { },
            onAccept = { },
            onReject = { },
            onViewDetails = { },
        )
    }
}
