package com.teobaranga.monica.data.user

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class UserApi(private val httpRequestMaker: HttpRequestMaker) {

    suspend fun getMe(): ApiResponse<MeResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/me")
        }
    }
}
