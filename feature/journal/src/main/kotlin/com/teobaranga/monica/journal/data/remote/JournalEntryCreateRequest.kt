package com.teobaranga.monica.journal.data.remote

import com.teobaranga.monica.core.data.adapter.LocalDateAsString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryCreateRequest(
    /**
     * A title for this journal entry. Max 255 characters.
     */
    @SerialName("title")
    val title: String?,
    /**
     * The content of the post. Max 1000000 characters.
     */
    @SerialName("post")
    val post: String,
    @SerialName("date")
    val date: LocalDateAsString,
)
