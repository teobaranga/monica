package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JournalApi {

    @GET("api/journal")
    suspend fun getJournal(
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null,
    ): ApiResponse<JournalEntriesResponse>
}
