package com.teobaranga.monica.journal.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object JournalEntriesRoute

fun NavGraphBuilder.journalEntries() {
    composable<JournalEntriesRoute> {
        JournalEntryList()
    }
}
