package com.teobaranga.monica.journal

import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.remote.JournalApi
import com.teobaranga.monica.journal.view.JournalEntryViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface TestJournalComponent {

    val journalEntryViewModelFactory: JournalEntryViewModel.Factory

    fun api(): JournalApi

    fun dao(): JournalDao

    fun tipsRepository(): TipsRepository
}
