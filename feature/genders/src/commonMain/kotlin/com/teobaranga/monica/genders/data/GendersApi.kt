package com.teobaranga.monica.genders.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

interface GendersApi {
    suspend fun getGenders(): ApiResponse<GendersResponse>
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class GendersApiImpl(private val httpRequestMaker: HttpRequestMaker) : GendersApi {

    override suspend fun getGenders(): ApiResponse<GendersResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/genders")
        }
    }
}
