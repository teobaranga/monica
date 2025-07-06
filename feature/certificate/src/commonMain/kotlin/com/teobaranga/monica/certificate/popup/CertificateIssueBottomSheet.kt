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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private val textButtonMinWidth = 64.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateIssueBottomSheet(
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
