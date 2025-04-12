package com.teobaranga.monica.journal.data.remote

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.deleteApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.skydoves.sandwich.ktor.postApiResponse
import com.skydoves.sandwich.ktor.putApiResponse
import com.teobaranga.monica.core.data.remote.DeleteResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class JournalApi(private val httpClient: HttpClient) {

    suspend fun getJournal(page: Int? = null, sort: String? = null): ApiResponse<JournalEntriesResponse> {
        return httpClient.getApiResponse("api/journal") {
            parameter("page", page)
            parameter("sort", sort)
        }
    }

    /**
     * Create a journal entry.
     */
    suspend fun createJournalEntry(request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse> {
        return httpClient.postApiResponse("api/journal") {
            setBody(request)
        }
    }

    /**
     * Update a journal entry.
     */
    suspend fun updateJournalEntry(id: Int, request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse> {
        return httpClient.putApiResponse("api/journal/$id") {
            setBody(request)
        }
    }

    /**
     * Delete a journal entry.
     */
    suspend fun deleteJournalEntry(id: Int): ApiResponse<DeleteResponse> {
        return httpClient.deleteApiResponse("api/journal/$id")
    }
}
