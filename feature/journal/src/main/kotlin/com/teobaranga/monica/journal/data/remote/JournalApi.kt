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
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

interface JournalApi {

    suspend fun getJournal(page: Int? = null, sort: String? = null): ApiResponse<JournalEntriesResponse>

    /**
     * Create a journal entry.
     */
    suspend fun createJournalEntry(request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse>

    /**
     * Update a journal entry.
     */
    suspend fun updateJournalEntry(id: Int, request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse>

    /**
     * Delete a journal entry.
     */
    suspend fun deleteJournalEntry(id: Int): ApiResponse<DeleteResponse>
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class JournalApiImpl(private val httpClient: HttpClient) : JournalApi {

    override suspend fun getJournal(page: Int?, sort: String?): ApiResponse<JournalEntriesResponse> {
        return httpClient.getApiResponse("api/journal") {
            parameter("page", page)
            parameter("sort", sort)
        }
    }

    override suspend fun createJournalEntry(request: JournalEntryCreateRequest): ApiResponse<JournalEntryResponse> {
        return httpClient.postApiResponse("api/journal") {
            setBody(request)
        }
    }

    override suspend fun updateJournalEntry(
        id: Int,
        request: JournalEntryCreateRequest,
    ): ApiResponse<JournalEntryResponse> {
        return httpClient.putApiResponse("api/journal/$id") {
            setBody(request)
        }
    }

    override suspend fun deleteJournalEntry(id: Int): ApiResponse<DeleteResponse> {
        return httpClient.deleteApiResponse("api/journal/$id")
    }
}
