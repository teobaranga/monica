package com.teobaranga.monica.contact.data.remote

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.deleteApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.skydoves.sandwich.ktor.putApiResponse
import com.teobaranga.monica.core.data.remote.DeleteResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class ContactApi(private val httpRequestMaker: HttpRequestMaker) {

    suspend fun getContacts(
        page: Int? = null,
        sort: String? = null,
    ): ApiResponse<MultipleContactsResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts") {
                parameter("page", page)
                parameter("sort", sort)
            }
        }
    }

    suspend fun getContact(id: Int): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts/$id")
        }
    }

    suspend fun createContact(request: CreateContactRequest): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts") {
                setBody(request)
            }
        }
    }

    suspend fun updateContact(id: Int, request: CreateContactRequest): ApiResponse<SingleContactResponse> {
        return httpRequestMaker.call {
            putApiResponse("api/contacts/$id") {
                setBody(request)
            }
        }
    }

    suspend fun deleteContact(id: Int): ApiResponse<DeleteResponse> {
        return httpRequestMaker.call {
            deleteApiResponse("api/contacts/$id")
        }
    }
}
