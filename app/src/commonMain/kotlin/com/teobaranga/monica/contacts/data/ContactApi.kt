package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.deleteApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.skydoves.sandwich.ktor.postApiResponse
import com.skydoves.sandwich.ktor.putApiResponse
import com.teobaranga.monica.activities.data.ContactActivitiesResponse
import com.teobaranga.monica.activities.data.CreateActivityRequest
import com.teobaranga.monica.activities.data.CreateActivityResponse
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

    suspend fun getContactActivities(
        id: Int,
        limit: Int? = null,
        page: Int? = null,
    ): ApiResponse<ContactActivitiesResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts/$id/activities") {
                parameter("limit", limit)
                parameter("page", page)
            }
        }
    }

    suspend fun createActivity(request: CreateActivityRequest): ApiResponse<CreateActivityResponse> {
        return httpRequestMaker.call {
            postApiResponse("api/activities") {
                setBody(request)
            }
        }
    }

    suspend fun updateActivity(id: Int, request: CreateActivityRequest): ApiResponse<CreateActivityResponse> {
        return httpRequestMaker.call {
            putApiResponse("api/activities/$id") {
                setBody(request)
            }
        }
    }

    suspend fun deleteActivity(id: Int): ApiResponse<DeleteResponse> {
        return httpRequestMaker.call {
            deleteApiResponse("api/activities/$id")
        }
    }
}
