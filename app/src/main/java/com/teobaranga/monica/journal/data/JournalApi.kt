package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    /**
     * Update a journal entry.
     */
    @PUT("api/journal/{id}")
    suspend fun updateJournalEntry(
        @Path("id") id: Int,
        @Body request: JournalEntryCreateRequest,
    ): ApiResponse<JournalEntryResponse>
}
