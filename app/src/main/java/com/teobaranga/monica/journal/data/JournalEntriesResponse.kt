package com.teobaranga.monica.journal.data

import com.teobaranga.monica.data.common.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntriesResponse(
    @SerialName("data")
    val data: List<JournalEntry>,
    @SerialName("meta")
    val meta: MetaResponse,
)
