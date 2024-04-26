package com.teobaranga.monica.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.spec.NavGraphSpec

enum class HomeTab(
    val icon: ImageVector,
    val label: String,
    val destination: NavGraphSpec,
) {
    DASHBOARD(
        icon = Icons.Default.Dashboard,
        label = "Dashboard",
        destination = NavGraphs.rootDashboard,
    ),
    CONTACTS(
        icon = Icons.Default.Groups,
        label = "Contacts",
        destination = NavGraphs.rootContacts,
    ),
    JOURNAL(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        label = "Journal",
        destination = NavGraphs.rootJournal,
    ),
}
