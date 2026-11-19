package com.teobaranga.monica.journal.list

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object JournalEntriesRoute : NavKey

fun EntryProviderScope<NavKey>.journalEntries() {
    entry<JournalEntriesRoute> {
        JournalEntryList()
    }
}
