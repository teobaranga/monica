package com.teobaranga.monica.journal.di

import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.local.JournalDatabaseOwner
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
object JournalComponent {

    @Provides
    fun providesJournalDao(owner: JournalDatabaseOwner): JournalDao = owner.journalDao()
}
