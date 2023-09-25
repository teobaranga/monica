package com.teobaranga.monica.journal.model

import java.time.ZonedDateTime

data class JournalEntry(
    val id: Int,
    val title: String?,
    val post: String,
    val date: ZonedDateTime,
    val created: ZonedDateTime,
    val updated: ZonedDateTime,
)
