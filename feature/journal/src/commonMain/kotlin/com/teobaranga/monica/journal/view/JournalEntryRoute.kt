package com.teobaranga.monica.journal.view

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryRoute(
    val entryId: Int? = null,
) : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class JournalEntryRouteSerializer : NavKeySerializer by navKeySerializer(JournalEntryRoute.serializer())

fun EntryProviderScope<NavKey>.journalEntry() {
    entry<JournalEntryRoute> { route ->
        JournalEntry(
            viewModel = assistedMetroViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory> {
                create(route.entryId)
            },
        )
    }
}
