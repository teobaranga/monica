package com.teobaranga.monica.journal.list

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
object JournalEntriesRoute : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class JournalEntriesRouteSerializer : NavKeySerializer by navKeySerializer(JournalEntriesRoute.serializer())

fun EntryProviderScope<NavKey>.journalEntries() {
    entry<JournalEntriesRoute> {
        JournalEntryList()
    }
}
