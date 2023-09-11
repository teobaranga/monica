package com.teobaranga.monica.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeTab(
    val icon: ImageVector,
    val label: String,
) {
    DASHBOARD(
        icon = Icons.Default.Dashboard,
        label = "Dashboard",
    ),
    CONTACTS(
        icon = Icons.Default.Groups,
        label = "Contacts",
    ),
    JOURNAL(
        icon = Icons.Default.MenuBook,
        label = "Journal",
    ),
}
