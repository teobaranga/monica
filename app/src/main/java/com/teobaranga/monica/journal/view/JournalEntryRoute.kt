package com.teobaranga.monica.journal.view

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryRoute(
    val entryId: Int? = null,
)

fun NavGraphBuilder.journalEntry() {
    composable<JournalEntryRoute> {
        JournalEntry()
    }
}
