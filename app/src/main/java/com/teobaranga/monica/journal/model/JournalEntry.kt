package com.teobaranga.monica.journal.model

import java.time.OffsetDateTime

data class JournalEntry(
    val id: Int,
    val title: String?,
    val post: String,
    val date: OffsetDateTime,
    val created: OffsetDateTime,
    val updated: OffsetDateTime,
)
