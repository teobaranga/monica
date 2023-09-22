package com.teobaranga.monica.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector
import com.teobaranga.monica.NavGraph
import com.teobaranga.monica.NavGraphs

enum class HomeTab(
    val icon: ImageVector,
    val label: String,
    val destination: NavGraph,
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
        icon = Icons.Default.MenuBook,
        label = "Journal",
        destination = NavGraphs.rootJournal,
    ),
}
