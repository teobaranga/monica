package com.teobaranga.monica.activity.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.deleteApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.skydoves.sandwich.ktor.postApiResponse
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
class ActivityApi(private val httpRequestMaker: HttpRequestMaker) {

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
