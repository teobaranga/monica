package com.teobaranga.monica.journal.data

import com.teobaranga.monica.data.adapter.OffsetDateTimeAsString
import com.teobaranga.monica.data.adapter.UuidAsString
import com.teobaranga.monica.data.common.AccountResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: UuidAsString,
    @SerialName("account")
    val account: AccountResponse,
    @SerialName("title")
    val title: String?,
    @SerialName("post")
    val post: String,
    @SerialName("date")
    val date: OffsetDateTimeAsString,
    @SerialName("created_at")
    val created: OffsetDateTimeAsString,
    @SerialName("updated_at")
    val updated: OffsetDateTimeAsString,
)
