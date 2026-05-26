package com.teobaranga.monica.contact.data.remote

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.deleteApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.skydoves.sandwich.ktor.postApiResponse
import com.skydoves.sandwich.ktor.putApiResponse
import com.teobaranga.monica.core.data.remote.DeleteResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

interface ContactApi {
    suspend fun getContacts(
        page: Int? = null,
        sort: String? = null,
    ): ApiResponse<MultipleContactsResponse>

    suspend fun getContact(id: Int): ApiResponse<SingleContactResponse>

    suspend fun createContact(request: CreateContactRequest): ApiResponse<SingleContactResponse>

    suspend fun updateContact(id: Int, request: CreateContactRequest): ApiResponse<SingleContactResponse>

    suspend fun deleteContact(id: Int): ApiResponse<DeleteResponse>
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ContactApiImpl(private val httpRequestMaker: HttpRequestMaker) : ContactApi {

    override suspend fun getContacts(
        page: Int?,
        sort: String?,
    ): ApiResponse<MultipleContactsResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts") {
                parameter("page", page)
                parameter("sort", sort)
            }
        }
    }

    override suspend fun getContact(id: Int): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts/$id")
        }
    }

    override suspend fun createContact(request: CreateContactRequest): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            postApiResponse("api/contacts") {
                setBody(request)
            }
        }
    }

    override suspend fun updateContact(id: Int, request: CreateContactRequest): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            putApiResponse("api/contacts/$id") {
                setBody(request)
            }
        }
    }

    override suspend fun deleteContact(id: Int): ApiResponse<DeleteResponse> {
        return httpRequestMaker.call {
            deleteApiResponse("api/contacts/$id")
        }
    }
}
