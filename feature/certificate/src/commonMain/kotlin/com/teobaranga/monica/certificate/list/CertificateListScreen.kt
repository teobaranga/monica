package com.teobaranga.monica.certificate.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.certificate.util.hexFormatDisplay
import com.teobaranga.monica.core.ui.icons.KeyboardArrowRight
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.io.bytestring.hexToByteString
import kotlinx.io.bytestring.toHexString
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun CertificateListScreen(
    type: CertificateListRoute.Type,
    certificateListItems: List<CertificateListItem>,
    onNavigateToCertificate: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {
                    when (type) {
                        CertificateListRoute.Type.UNTRUSTED -> {
                            Text(text = "Untrusted certificates")
                        }
                        CertificateListRoute.Type.TRUSTED -> {
                            Text(text = "Custom trusted certificates")
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(certificateListItems) { userCertificate ->
                ListItem(
                    modifier = Modifier
                        .clickable {
                            onNavigateToCertificate(userCertificate.sha256Hash.toHexString())
                        },
                    headlineContent = {
                        Text(text = userCertificate.issuer)
                    },
                    supportingContent = {
                        val expiryDate = userCertificate.expiry.format(DateTimeComponents.Formats.RFC_1123)
                        if (userCertificate.isExpired) {
                            Text(
                                text = "Expired: $expiryDate",
                                color = MaterialTheme.colorScheme.error,
                            )
                        } else {
                            Text(text = "Expires: $expiryDate")
                        }
                    },
                    trailingContent = {
                        Icon(
                            imageVector = KeyboardArrowRight,
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Preview
@Composable
private fun CertificateListScreenPreview() {
    MonicaTheme {
        CertificateListScreen(
            type = CertificateListRoute.Type.UNTRUSTED,
            certificateListItems = listOf(
                CertificateListItem(
                    sha256Hash = "DE:AD:BE:EF".hexToByteString(hexFormatDisplay),
                    issuer = "example.com",
                    expiry = Clock.System.now(),
                    isExpired = false,
                ),
                CertificateListItem(
                    sha256Hash = "DE:AD:BE:EF".hexToByteString(hexFormatDisplay),
                    issuer = "another-example.com",
                    expiry = Clock.System.now(),
                    isExpired = true,
                ),
            ),
            onNavigateToCertificate = { },
            onBack = { },
        )
    }
}
