package com.teobaranga.monica.journal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.monica.journal.list.JournalEntryList
import kotlinx.serialization.Serializable

@Serializable
object JournalEntriesRoute

fun NavGraphBuilder.journalEntries() {
    composable<JournalEntriesRoute> {
        JournalEntryList()
    }
}
