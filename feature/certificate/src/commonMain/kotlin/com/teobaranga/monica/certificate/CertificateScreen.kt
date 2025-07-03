package com.teobaranga.monica.certificate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.clipboard.setPlainText
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateScreen(
    certificateData: CertificateData,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Certificate Details") },
            )
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { paddingValues ->
        CertificateDetails(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            certificateData = certificateData,
        )
    }
}

@Composable
private fun CertificateDetails(
    certificateData: CertificateData,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        certificateData.subjectName?.let {
            item {
                SubjectNameSection(it)
            }
        }
        item {
            IssuerNameSection(certificateData.issuerName)
        }
        item {
            ValiditySection(certificateData.validity)
        }
        item {
            PublicKeyInfoSection(certificateData.publicKeyInfo)
        }
        item {
            MiscellaneousSection(certificateData.miscellaneous)
        }
        item {
            FingerprintsSection(certificateData.fingerprints)
        }
        item {
            BasicConstraintsSection(certificateData.basicConstraints)
        }
        item {
            KeyUsagesSection(certificateData.keyUsages)
        }
        item {
            CertificateDetailItem(
                headlineText = "Subject Key ID",
                supportingText = certificateData.subjectKeyId,
                isCopyEnabled = true,
            )
        }
        item {
            CertificateDetailItem(
                headlineText = "Authority Key ID",
                supportingText = certificateData.authorityKeyId,
                isCopyEnabled = true,
            )
        }
    }
}

@Composable
private fun SubjectNameSection(subjectName: SubjectName) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = "Subject Name",
                style = MaterialTheme.typography.titleMedium,
            )
            CertificateDetailItem(
                headlineText = "Common Name",
                supportingText = subjectName.commonName,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun IssuerNameSection(issuerName: IssuerName) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = "Issuer Name",
                style = MaterialTheme.typography.titleMedium,
            )
            CertificateDetailItem(
                headlineText = "Common Name",
                supportingText = issuerName.commonName,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ValiditySection(validity: Validity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = "Validity",
                style = MaterialTheme.typography.titleMedium,
            )
            CertificateDetailItem(
                headlineText = "Not Before",
                supportingText = validity.notBefore.toString(),
            )
            CertificateDetailItem(
                headlineText = "Not After",
                supportingText = validity.notAfter.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PublicKeyInfoSection(publicKeyInfo: PublicKeyInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = "Public Key Info",
                style = MaterialTheme.typography.titleMedium,
            )
            CertificateDetailItem(
                headlineText = "Algorithm",
                supportingText = publicKeyInfo.algorithm,
            )
            CertificateDetailItem(
                headlineText = "Key Size",
                supportingText = publicKeyInfo.keySize.toString(),
            )
            CertificateDetailItem(
                headlineText = "Public Value",
                supportingText = publicKeyInfo.publicValue,
                isCopyEnabled = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MiscellaneousSection(miscellaneous: Miscellaneous) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = "Miscellaneous",
                style = MaterialTheme.typography.titleMedium,
            )
            CertificateDetailItem(
                headlineText = "Serial Number",
                supportingText = miscellaneous.serialNumber,
                isCopyEnabled = true,
            )
            CertificateDetailItem(
                headlineText = "Signature Algorithm",
                supportingText = miscellaneous.signatureAlgorithm,
            )
            CertificateDetailItem(
                headlineText = "Version",
                supportingText = miscellaneous.version.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FingerprintsSection(fingerprints: Fingerprints) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                text = "Fingerprints",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            CertificateDetailItem(
                headlineText = "SHA-256",
                supportingText = fingerprints.sha256,
                isCopyEnabled = true,
            )
            CertificateDetailItem(
                headlineText = "SHA-1",
                supportingText = fingerprints.sha1,
                isCopyEnabled = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BasicConstraintsSection(basicConstraints: BasicConstraints) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                text = "Basic Constraints",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            CertificateDetailItem(
                headlineText = "Certificate Authority",
                supportingText = if (basicConstraints.certificateAuthority) "Yes" else "No",
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun KeyUsagesSection(keyUsages: KeyUsages) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                text = "Key Usages",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            CertificateDetailItem(
                headlineText = "Purposes",
                supportingText = keyUsages.purposes.joinToString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CertificateDetailItem(
    headlineText: String,
    supportingText: String,
    isCopyEnabled: Boolean = false,
) {
    ListItem(
        modifier = Modifier.padding(horizontal = 16.dp),
        headlineContent = { Text(text = headlineText) },
        supportingContent = { Text(text = supportingText) },
        trailingContent = if (isCopyEnabled) {
            {
                val coroutineScope = rememberCoroutineScope()
                val clipboard = LocalClipboard.current
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            clipboard.setPlainText(supportingText)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy $headlineText",
                    )
                }
            }
        } else {
            null
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Preview
@Composable
private fun CertificateScreenPreview() {
    MonicaTheme {
        CertificateScreen(
            certificateData = CertificateData(
                subjectName = SubjectName(
                    commonName = "Caddy Local Authority - ECC Intermediate",
                ),
                issuerName = IssuerName(
                    commonName = "Caddy Local Authority - 2025 ECC Root",
                ),
                validity = Validity(
                    notBefore = Clock.System.now(),
                    notAfter = Clock.System.now(),
                ),
                publicKeyInfo = PublicKeyInfo(
                    algorithm = "Elliptic Curve",
                    keySize = 256,
                    publicValue = "04:64:3E:44:89:EA:3A:F3:94:FA:B0:2E:45:89:2E:76:FE:01:7A:EF:93:34:15:91:...",
                ),
                miscellaneous = Miscellaneous(
                    serialNumber = "3A:D7:31:87:D9:D1:F8:BF:FB:14:39:50:EA:4A:60:50",
                    signatureAlgorithm = "ECDSA with SHA-256",
                    version = 3,
                ),
                fingerprints = Fingerprints(
                    sha256 = "66:0A:EC:0F:4C:2E:2C:CE:D7:AA:BF:D4:A8:DE:28:A1:4C:74:2B:C1:0D:FB:FE:...",
                    sha1 = "52:E2:5C:7E:1F:6F:A0:22:5D:9C:41:13:E0:55:12:4E:5C:3F:1A:AF",
                ),
                basicConstraints = BasicConstraints(
                    certificateAuthority = true,
                ),
                keyUsages = KeyUsages(
                    purposes = listOf("Certificate Signing", "CRL Signing"),
                ),
                subjectKeyId = "E4:A6:95:C2:A8:1B:85:4E:6C:DC:69:BF:F9:D7:EB:32:28:92:CC:45",
                authorityKeyId = "C8:F1:F9:43:1E:1D:5F:26:AA:12:E6:F9:CF:A2:1F:FB:23:57:B3:AE",
            ),
        )
    }
}
