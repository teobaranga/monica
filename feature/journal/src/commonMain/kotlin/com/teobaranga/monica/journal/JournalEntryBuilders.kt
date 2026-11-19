package com.teobaranga.monica.journal

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.journal.list.journalEntries
import com.teobaranga.monica.journal.view.journalEntry
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

/**
 * Contributes the journal destinations to the app's navigation entries.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object JournalEntryBuilders {

    @Provides
    @IntoSet
    fun provideJournalEntries(): EntryProviderScope<NavKey>.() -> Unit = {
        journalEntries()
        journalEntry()
    }
}
