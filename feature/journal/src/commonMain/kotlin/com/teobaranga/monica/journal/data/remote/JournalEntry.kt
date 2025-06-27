package com.teobaranga.monica.journal.data.remote

import com.teobaranga.monica.core.data.adapter.UuidAsString
import com.teobaranga.monica.core.data.remote.AccountResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

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
    val date: Instant,
    @SerialName("created_at")
    val created: Instant,
    @SerialName("updated_at")
    val updated: Instant,
)
