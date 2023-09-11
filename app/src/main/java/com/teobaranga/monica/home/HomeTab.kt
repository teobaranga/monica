package com.teobaranga.monica.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.teobaranga.monica.destinations.ContactsDestination
import com.teobaranga.monica.destinations.DashboardDestination
import com.teobaranga.monica.destinations.JournalDestination

enum class HomeTab(
    val icon: ImageVector,
    val label: String,
    val destination: DirectionDestinationSpec,
) {
    DASHBOARD(
        icon = Icons.Default.Dashboard,
        label = "Dashboard",
        destination = DashboardDestination,
    ),
    CONTACTS(
        icon = Icons.Default.Groups,
        label = "Contacts",
        destination = ContactsDestination,
    ),
    JOURNAL(
        icon = Icons.Default.MenuBook,
        label = "Journal",
        destination = JournalDestination,
    ),
}
