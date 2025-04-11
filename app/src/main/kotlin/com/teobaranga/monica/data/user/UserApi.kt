package com.teobaranga.monica.data.user

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class UserApi(private val httpClient: HttpClient) {

    suspend fun getMe(): ApiResponse<MeResponse> {
        return httpClient.getApiResponse("api/me")
    }
}
