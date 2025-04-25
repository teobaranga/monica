package com.teobaranga.monica.journal.di

import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.local.JournalDatabaseOwner
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface JournalComponent {

    @Provides
    fun providesJournalDao(owner: JournalDatabaseOwner): JournalDao = owner.journalDao()
}
