package com.teobaranga.monica.journal.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JournalEntryResponse(
    @Json(name = "data")
    val data: JournalEntry,
)
