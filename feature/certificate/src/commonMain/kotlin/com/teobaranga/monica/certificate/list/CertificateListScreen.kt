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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateListScreen(
    userCertificates: List<UserCertificate>,
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
                    Text(text = "Untrusted certificates")
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(userCertificates) { userCertificate ->
                ListItem(
                    modifier = Modifier
                        .clickable {
                            onNavigateToCertificate(userCertificate.sha256Hash)
                        },
                    overlineContent = {
                        Text(text = "Issuer")
                    },
                    headlineContent = {
                        Text(text = userCertificate.issuer)
                    },
                    supportingContent = {
                        val expiryDate = userCertificate.expiry.format(DateTimeComponents.Formats.RFC_1123)
                        Text(text = "Expires: $expiryDate")
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CertificateListScreenPreview() {
    MonicaTheme {
        CertificateListScreen(
            userCertificates = listOf(
                UserCertificate(
                    sha256Hash = "00:00:00:00",
                    issuer = "example.com",
                    expiry = Clock.System.now(),
                ),
                UserCertificate(
                    sha256Hash = "00:00:00:00",
                    issuer = "another-example.com",
                    expiry = Clock.System.now(),
                ),
            ),
            onNavigateToCertificate = { },
            onBack = { },
        )
    }
}
