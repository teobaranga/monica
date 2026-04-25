package com.teobaranga.monica.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class AccountPreviewParameterProvider: PreviewParameterProvider<AccountUiState> {

    override val values: Sequence<AccountUiState>
        get() = sequenceOf(
            AccountUiState(
                email = "johndoe@mail.com",
                actionGroups = listOf(
                    ActionItem(
                        label = "Server",
                        icon = Icons.Outlined.Wifi,
                        children = listOf(
                            SelectableItem(
                                overline = "Production",
                                label = "https://app.monicahq.com",
                                selected = true,
                            ),
                            ActionItem(label = "Manage servers", action = AccountAction.ManageServers),
                        ),
                    ),
                    ActionItem(
                        label = "Switch account",
                        icon = Icons.Outlined.SyncAlt,
                        children = listOf(
                            SelectableItem(
                                overline = "John Doe",
                                label = "johndoe@mail.com",
                                selected = true,
                            ),
                            ActionItem(label = "Manage accounts", action = AccountAction.ManageAccounts),
                        ),
                    ),
                    ActionItem(
                        label = "Sign out",
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        action = AccountAction.SignOut,
                    ),
                    ActionItem(
                        label = "Settings",
                        icon = Icons.Outlined.Settings,
                        action = AccountAction.OpenSettings,
                    ),
                ),
            )
        )
}
