package com.teobaranga.monica.journal.list.ui

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

sealed interface JournalEntryListItem {

    @Stable
    data class Entry(
        val id: Int,
        val title: String?,
        val post: String,
        val date: LocalDate,
    ) : JournalEntryListItem

    data class SectionTitle(
        val month: Month,
        val year: Int?,
    ) : JournalEntryListItem

    data object Divider : JournalEntryListItem
}
