package com.teobaranga.monica.journal.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.data.common.AccountResponse
import java.time.OffsetDateTime
import java.util.UUID

@JsonClass(generateAdapter = true)
data class JournalEntry(
    @Json(name = "id")
    val id: Int,
    @Json(name = "uuid")
    val uuid: UUID,
    @Json(name = "account")
    val account: AccountResponse,
    @Json(name = "title")
    val title: String?,
    @Json(name = "post")
    val post: String,
    @Json(name = "date")
    val date: OffsetDateTime,
    @Json(name = "created_at")
    val created: OffsetDateTime,
    @Json(name = "updated_at")
    val updated: OffsetDateTime,
)
