package com.teobaranga.monica.journal

import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.remote.JournalApi
import com.teobaranga.monica.journal.view.JournalEntryViewModel
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface TestJournalComponent {

    fun journalEntryViewModel(): (SavedStateHandle) -> JournalEntryViewModel

    fun api(): JournalApi

    fun dao(): JournalDao

    fun tipsRepository(): TipsRepository
}
