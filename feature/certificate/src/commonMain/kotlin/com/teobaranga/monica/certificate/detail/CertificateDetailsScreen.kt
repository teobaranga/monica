package com.teobaranga.monica.certificate.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.clipboard.setPlainText
import com.teobaranga.monica.core.ui.icons.Delete
import com.teobaranga.monica.core.ui.plus
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.util.debounce
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateDetailsScreen(
    certificateData: CertificateDetailsUiState,
    onBack: () -> Unit,
    onDelete: () -> Unit,
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
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
                title = { Text(text = "Certificate Details") },
                actions = {
                    if (certificateData.isTrusted) {
                        IconButton(
                            onClick = {
                                showDeleteConfirmDialog = true
                            },
                        ) {
                            Icon(
                                imageVector = Delete,
                                contentDescription = "Delete certificate",
                            )
                        }
                    }
                }
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

    if (showDeleteConfirmDialog) {
        DeleteAlertDialog(
            onConfirm = {
                showDeleteConfirmDialog = false
                onDelete()
            },
            onCancel = {
                showDeleteConfirmDialog = false
            },
        )
    }
}

@Composable
private fun CertificateDetails(
    certificateData: CertificateDetailsUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp) +
            WindowInsets.navigationBars.asPaddingValues(),
    ) {
        certificateData.sections.forEach { section ->
            item {
                Section(
                    section = section,
                )
            }
        }
    }
}

@Composable
private fun Section(section: CertificateDetailsUiState.Section) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = section.title,
                style = MaterialTheme.typography.titleMedium,
            )
            section.items.forEach { item ->
                Item(
                    item = item,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Item(
    item: CertificateDetailsUiState.Section.Item,
) {
    ListItem(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        headlineContent = { Text(text = item.title) },
        supportingContent = { Text(text = item.value) },
        trailingContent = if (item.isCopyEnabled) {
            {
                val coroutineScope = rememberCoroutineScope()
                val clipboard = LocalClipboard.current
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            clipboard.setPlainText(item.value)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy ${item.title}",
                    )
                }
            }
        } else {
            null
        },
    )
}

@Composable
private fun DeleteAlertDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = "Delete")
        },
        text = {
            Text(text = "Are you sure you want to delete this certificate?")
        },
        confirmButton = {
            TextButton(
                onClick = debounce(action = onConfirm),
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = debounce(action = onCancel),
            ) {
                Text(text = "Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun CertificateScreenPreview() {
    MonicaTheme {
        CertificateDetailsScreen(
            certificateData = CertificateDetailsUiState(
                isTrusted = true,
                sections = listOf(
                    CertificateDetailsUiState.Section(
                        title = "Subject",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Common Name",
                                value = "Caddy Local Authority - ECC Intermediate",
                            ),
                        ),
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Issuer",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Common Name",
                                value = "Caddy Local Authority - 2025 ECC Root",
                            ),
                        ),
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Validity",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Not Before",
                                value = "2023-05-25 16:00:00 UTC",
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "Not After",
                                value = "2024-05-25 16:00:00 UTC",
                            )
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Public Key Info",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Algorithm",
                                value = "Elliptic Curve",
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "Key Size",
                                value = "256",
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "Public Value",
                                value = "04:64:3E:44:89:EA:3A:F3:94:FA:B0:2E:45:89:2E:76:FE:01:7A:EF:93:34:15:91:...",
                            )
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Miscellaneous",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Serial Number",
                                value = "3A:D7:31:87:D9:D1:F8:BF:FB:14:39:50:EA:4A:60:50",
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "Signature Algorithm",
                                value = "ECDSA with SHA-256",
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "Version",
                                value = "3",
                            ),
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Fingerprints",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "SHA-256",
                                value = "66:0A:EC:0F:4C:2E:2C:CE:D7:AA:BF:D4:A8:DE:28:A1:4C:74:2B:C1:0D:FB:FE:...",
                                isCopyEnabled = true,
                            ),
                            CertificateDetailsUiState.Section.Item(
                                title = "SHA-1",
                                value = "52:E2:5C:7E:1F:6F:A0:22:5D:9C:41:13:E0:55:12:4E:5C:3F:1A:AF",
                            ),
                        ),
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Key Usages",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Purposes",
                                value = "Digital Signature",
                            ),
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Extended Key Usages",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Purposes",
                                value = "Server Authentication, Client Authentication",
                            ),
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Authority Key ID",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Key ID",
                                value = "4A:E4:89:62:A8:31:6B:0A:F5:E5:8A:AE:85:7E:FF:E3:72:94:A2:91",
                            ),
                        )
                    ),
                    CertificateDetailsUiState.Section(
                        title = "Subject Key ID",
                        items = listOf(
                            CertificateDetailsUiState.Section.Item(
                                title = "Key ID",
                                value = "4A:E4:89:62:A8:31:6B:0A:F5:E5:8A:AE:85:7E:FF:E3:72:94:A2:91",
                            ),
                        )
                    ),
                )
            ),
            onBack = { },
            onDelete = { },
        )
    }
}
