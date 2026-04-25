package com.teobaranga.monica.account

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.account.nav.AccountRoute
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.plus
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.user.data.local.MeFullDetails

fun NavGraphBuilder.account() {
    composable<AccountRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            )
        }
    ) {
        Account()
    }
}

@Composable
fun Account(
    viewModel: AccountViewModel = injectedViewModel(),
) {
    val navigator = LocalNavigator.current
    val uiState = viewModel.uiState
    val meFullDetails by viewModel.userAvatar.collectAsStateWithLifecycle()
    AccountScreen(
        meFullDetails = meFullDetails,
        uiState = uiState,
        onActionClick = { action ->
            when (action) {
                AccountAction.ManageServers -> {
                    navigator.navigate(CertificateListRoute(CertificateTrustStatus.TRUSTED)) {
                        popUpTo<AccountRoute> {
                            inclusive = true
                        }
                    }
                }

                AccountAction.SignOut -> {
                    viewModel.onClearAuthorization()
                }

                AccountAction.ManageAccounts,
                AccountAction.OpenSettings,
                -> Unit
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    meFullDetails: MeFullDetails?,
    uiState: AccountUiState,
    onActionClick: (AccountAction) -> Unit,
) {
    Scaffold { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentPadding = contentPadding
                + PaddingValues(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                PlaceholderAvatar()
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = meFullDetails?.info?.email ?: "...",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            uiState.actionGroups.forEach { actionGroup ->
                item {
                    ActionGroupCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 8.dp),
                        actionGroup = actionGroup,
                        onActionClick = onActionClick,
                    )
                }
            }
            item {
                val uriHandler = LocalUriHandler.current
                FooterLinks(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    onOpenDocumentation = {
                        uriHandler.openUri("https://monica.teobaranga.com")
                    },
                    onOpenGithub = {
                        uriHandler.openUri("https://github.com/teobaranga/monica")
                    },
                    onOpenLicenses = {
                        // TODO
                    },
                )
            }
        }
    }
}

@Composable
private fun PlaceholderAvatar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(128.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
    )
}

@Composable
private fun ActionGroupCard(
    actionGroup: ActionItem,
    onActionClick: (AccountAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        var isExpanded by remember { mutableStateOf(true) }
        ActionRow(
            icon = actionGroup.icon,
            label = actionGroup.label,
            onClick = {
                isExpanded = !isExpanded
            },
            trailingIcon = if (actionGroup.children.isNotEmpty()) {
                if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
            } else {
                null
            },
        )

        if (isExpanded && actionGroup.children.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            actionGroup.children.forEach { item ->
                GroupItem(
                    item = item,
                    onActionClick = onActionClick,
                )
            }
        }
    }
}

@Composable
private fun GroupItem(
    item: AccountGroupItem,
    onActionClick: (AccountAction) -> Unit,
) {
    when (item) {
        is ActionItem -> {
            ActionRow(
                icon = item.icon,
                label = item.label,
                onClick = {
                    item.action?.let { onActionClick(it) }
                },
            )
        }

        is SelectableItem -> {
            SelectableDetailRow(
                overline = item.overline,
                headline = item.label,
                selected = item.selected,
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector?,
    label: String,
    onClick: () -> Unit,
    trailingIcon: ImageVector? = null,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        leadingContent = {
            if (icon == null) {
                Spacer(modifier = Modifier.size(24.dp))
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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
private fun SelectableDetailRow(
    overline: String,
    headline: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        leadingContent = {
            Spacer(modifier = Modifier.size(24.dp))
        },
        overlineContent = {
            Text(
                text = overline,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        headlineContent = {
            Text(text = headline)
        },
        trailingContent = {
            RadioButton(
                selected = selected,
                onClick = {
                    // TODO
                },
            )
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
private fun PreviewAccountScreen(
    @PreviewParameter(provider = AccountPreviewParameterProvider::class)
    uiState: AccountUiState,
) {
    MonicaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            AccountScreen(
                meFullDetails = null,
                uiState = uiState,
                onActionClick = { },
            )
        }
    }
}
