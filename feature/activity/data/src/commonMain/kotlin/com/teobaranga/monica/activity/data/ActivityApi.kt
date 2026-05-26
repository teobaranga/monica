package com.teobaranga.monica.activity.data

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

interface ActivityApi {
    suspend fun getContactActivities(
        id: Int,
        limit: Int? = null,
        page: Int? = null,
    ): ApiResponse<ContactActivitiesResponse>

    suspend fun createActivity(request: CreateActivityRequest): ApiResponse<CreateActivityResponse>

    suspend fun updateActivity(id: Int, request: CreateActivityRequest): ApiResponse<CreateActivityResponse>

    suspend fun deleteActivity(id: Int): ApiResponse<DeleteResponse>
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ActivityApiImpl(private val httpRequestMaker: HttpRequestMaker) : ActivityApi {

    override suspend fun getContactActivities(
        id: Int,
        limit: Int?,
        page: Int?,
    ): ApiResponse<ContactActivitiesResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts/$id/activities") {
                parameter("limit", limit)
                parameter("page", page)
            }
        }
    }

    override suspend fun createActivity(request: CreateActivityRequest): ApiResponse<CreateActivityResponse> {
        return httpRequestMaker.call {
            postApiResponse("api/activities") {
                setBody(request)
            }
        }
    }

    override suspend fun updateActivity(id: Int, request: CreateActivityRequest): ApiResponse<CreateActivityResponse> {
        return httpRequestMaker.call {
            putApiResponse("api/activities/$id") {
                setBody(request)
            }
        }
    }

    override suspend fun deleteActivity(id: Int): ApiResponse<DeleteResponse> {
        return httpRequestMaker.call {
            deleteApiResponse("api/activities/$id")
        }
    }
}
