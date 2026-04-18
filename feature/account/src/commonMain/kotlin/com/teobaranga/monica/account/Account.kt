package com.teobaranga.monica.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme

private const val documentationUrl = "https://monica.teobaranga.com"
private const val githubUrl = "https://github.com/teobaranga/monica"
private const val licensesUrl = "https://github.com/teobaranga/monica/blob/main/LICENSE"

@Composable
fun Account(
    viewModel: AccountViewModel = injectedViewModel(),
    onDismissRequest: () -> Unit,
) {
    val navigator = LocalNavigator.current
    val uriHandler = LocalUriHandler.current
    AccountScreen(
        onClearAuthorization = {
            viewModel.onClearAuthorization()
            onDismissRequest()
        },
        onViewCertificates = {
            navigator.navigate(CertificateListRoute(CertificateTrustStatus.TRUSTED))
            onDismissRequest()
        },
        onOpenDocumentation = {
            uriHandler.openUri(documentationUrl)
        },
        onOpenGithub = {
            uriHandler.openUri(githubUrl)
        },
        onOpenLicenses = {
            uriHandler.openUri(licensesUrl)
        },
        onDismissRequest = onDismissRequest,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    onClearAuthorization: () -> Unit,
    onViewCertificates: () -> Unit,
    onOpenDocumentation: () -> Unit,
    onOpenGithub: () -> Unit,
    onOpenLicenses: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                PlaceholderAvatar(
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "johndoe@mail.com",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            item {
                ActionGroup(
                    modifier = Modifier.padding(top = 12.dp),
                    items = listOf(
                        ActionItem(
                            icon = Icons.Outlined.Wifi,
                            label = "Server",
                            onClick = onViewCertificates,
                            trailingIcon = Icons.Filled.KeyboardArrowDown,
                        ),
                    ),
                )
            }
            item {
                ActionGroup(
                    modifier = Modifier.padding(top = 12.dp),
                    items = listOf(
                        ActionItem(
                            icon = Icons.Outlined.SyncAlt,
                            label = "Switch account",
                            onClick = {
                                // TODO
                            },
                            trailingIcon = Icons.Filled.KeyboardArrowDown,
                        ),
                        ActionItem(
                            icon = Icons.AutoMirrored.Outlined.Logout,
                            label = "Sign out",
                            onClick = onClearAuthorization,
                        ),
                    ),
                )
            }
            item {
                ActionGroup(
                    modifier = Modifier.padding(top = 12.dp),
                    items = listOf(
                        ActionItem(
                            icon = Icons.Outlined.Settings,
                            label = "Settings",
                            onClick = {
                                // TODO
                            },
                        ),
                    ),
                )
            }
            item {
                FooterLinks(
                    modifier = Modifier.padding(top = 16.dp),
                    onOpenDocumentation = onOpenDocumentation,
                    onOpenGithub = onOpenGithub,
                    onOpenLicenses = onOpenLicenses,
                )
            }
        }
    }
}

@Composable
private fun PlaceholderAvatar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(128.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
    )
}

private data class ActionItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    val trailingIcon: ImageVector? = null,
)

@Composable
private fun ActionGroup(
    items: List<ActionItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            ActionRow(
                icon = item.icon,
                label = item.label,
                onClick = item.onClick,
                trailingIcon = item.trailingIcon,
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    trailingIcon: ImageVector? = null,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        headlineContent = {
            Text(text = label)
        },
        trailingContent = trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun FooterLinks(
    onOpenDocumentation: () -> Unit,
    onOpenGithub: () -> Unit,
    onOpenLicenses: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FooterLink(text = "Documentation", onClick = onOpenDocumentation)
        FooterBullet()
        FooterLink(text = "GitHub", onClick = onOpenGithub)
        FooterBullet()
        FooterLink(text = "Licenses", onClick = onOpenLicenses)
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier.clickable(onClick = onClick),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun FooterBullet() {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = "•",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
    )
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
                onOpenDocumentation = { },
                onOpenGithub = { },
                onOpenLicenses = { },
                onDismissRequest = { },
            )
        }
    }
}
