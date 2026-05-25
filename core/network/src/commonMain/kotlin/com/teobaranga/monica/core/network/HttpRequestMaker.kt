package com.teobaranga.monica.core.network

import com.skydoves.sandwich.ApiResponse
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient

@Inject
class HttpRequestMaker(
    private val httpClient: () -> HttpClient,
    private val apiExceptionHandler: ApiExceptionHandler,
) {

    suspend fun <T> call(
        block: suspend HttpClient.() -> ApiResponse<T>,
    ): ApiResponse<T> {
        var response = httpClient().block()
        if (response is ApiResponse.Failure.Exception) {
            response = apiExceptionHandler.handle(response, block)
        }
        return response
    }
}
