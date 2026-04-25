package com.teobaranga.monica.account

import androidx.compose.ui.graphics.vector.ImageVector

data class AccountUiState(
    val email: String,
    val actionGroups: List<ActionItem>,
)

sealed interface AccountGroupItem

data class ActionItem(
    val label: String,
    val icon: ImageVector? = null,
    val action: AccountAction? = null,
    val children: List<AccountGroupItem> = emptyList(),
) : AccountGroupItem

data class SelectableItem(
    val overline: String,
    val label: String,
    val selected: Boolean,
) : AccountGroupItem

enum class AccountAction {
    ManageServers,
    ManageAccounts,
    SignOut,
    OpenSettings,
}
