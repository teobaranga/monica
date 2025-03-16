package com.teobaranga.monica.journal.data.remote

import com.teobaranga.monica.core.data.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntriesResponse(
    @SerialName("data")
    val data: List<JournalEntry>,
    @SerialName("meta")
    val meta: MetaResponse,
)
