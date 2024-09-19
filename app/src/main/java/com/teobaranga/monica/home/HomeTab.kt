package com.teobaranga.monica.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.generated.navgraphs.ContactsGraph
import com.ramcosta.composedestinations.generated.navgraphs.DashboardGraph
import com.ramcosta.composedestinations.generated.navgraphs.JournalGraph
import com.ramcosta.composedestinations.spec.DirectionNavGraphSpec

enum class HomeTab(
    val icon: ImageVector,
    val label: String,
    val destination: DirectionNavGraphSpec,
) {
    DASHBOARD(
        icon = Icons.Default.Dashboard,
        label = "Dashboard",
        destination = DashboardGraph,
    ),
    CONTACTS(
        icon = Icons.Default.Groups,
        label = "Contacts",
        destination = ContactsGraph,
    ),
    JOURNAL(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        label = "Journal",
        destination = JournalGraph,
    ),
}
