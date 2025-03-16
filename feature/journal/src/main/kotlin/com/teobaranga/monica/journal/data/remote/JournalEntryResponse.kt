package com.teobaranga.monica.journal.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryResponse(
    @SerialName("data")
    val data: JournalEntry,
)
