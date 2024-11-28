package com.teobaranga.monica.journal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryResponse(
    @SerialName("data")
    val data: JournalEntry,
)
