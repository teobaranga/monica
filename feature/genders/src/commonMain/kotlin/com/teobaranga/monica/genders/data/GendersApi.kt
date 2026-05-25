package com.teobaranga.monica.genders.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
class GendersApi(private val httpRequestMaker: HttpRequestMaker) {

    suspend fun getGenders(): ApiResponse<GendersResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/genders")
        }
    }
}
