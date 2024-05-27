package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class JournalEntryCreateRequest(
    /**
     * A title for this journal entry. Max 255 characters.
     */
    @Json(name = "title")
    val title: String,
    /**
     * The content of the post. Max 1000000 characters.
     */
    @Json(name = "post")
    val post: String,
    @Json(name = "date")
    val date: OffsetDateTime,
)

interface JournalApi {

    @GET("api/journal")
    suspend fun getJournal(
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null,
    ): ApiResponse<JournalEntriesResponse>

    /**
     * Create a journal entry.
     */
    @POST("api/journal")
    suspend fun createJournalEntry(@Body request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse>
}
