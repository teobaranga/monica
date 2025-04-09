package com.teobaranga.monica.genders.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class GendersApi(private val httpClient: HttpClient) {

    suspend fun getGenders(): ApiResponse<GendersResponse> {
        return httpClient.getApiResponse("api/genders")
    }
}
