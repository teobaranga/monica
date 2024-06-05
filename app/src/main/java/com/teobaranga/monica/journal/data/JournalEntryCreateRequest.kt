package com.teobaranga.monica.journal.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class JournalEntryCreateRequest(
    /**
     * A title for this journal entry. Max 255 characters.
     */
    @Json(name = "title")
    val title: String?,
    /**
     * The content of the post. Max 1000000 characters.
     */
    @Json(name = "post")
    val post: String,
    @Json(name = "date")
    val date: LocalDate,
)
