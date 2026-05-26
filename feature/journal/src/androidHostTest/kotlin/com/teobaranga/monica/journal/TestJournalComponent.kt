package com.teobaranga.monica.journal

import com.teobaranga.monica.account.AccountViewModel
import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.remote.JournalApi
import com.teobaranga.monica.journal.view.JournalEntryViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(
    scope = AppScope::class,
    excludes = [
        AccountViewModel::class,
    ],
)
interface TestJournalComponent {

    val journalEntryViewModelFactory: JournalEntryViewModel.Factory

    fun api(): JournalApi

    fun dao(): JournalDao

    fun tipsRepository(): TipsRepository
}
