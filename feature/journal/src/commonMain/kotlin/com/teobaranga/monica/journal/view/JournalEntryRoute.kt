package com.teobaranga.monica.journal.view

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryRoute(
    val entryId: Int? = null,
) : NavKey

fun EntryProviderScope<NavKey>.journalEntry() {
    entry<JournalEntryRoute> { route ->
        JournalEntry(
            viewModel = assistedMetroViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory> {
                create(route.entryId)
            },
        )
    }
}
