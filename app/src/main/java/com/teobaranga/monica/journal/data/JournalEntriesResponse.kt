package com.teobaranga.monica.journal.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.data.common.MetaResponse

@JsonClass(generateAdapter = true)
data class JournalEntriesResponse(
    @Json(name = "data")
    val data: List<JournalEntryResponse>,
    @Json(name = "meta")
    val meta: MetaResponse,
)
