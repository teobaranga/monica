package com.teobaranga.monica.journal.list.ui

import androidx.compose.runtime.Stable
import java.time.OffsetDateTime

@Stable
data class JournalEntryListItem(
    val id: Int,
    val title: String?,
    val post: String,
    val date: OffsetDateTime,
)
