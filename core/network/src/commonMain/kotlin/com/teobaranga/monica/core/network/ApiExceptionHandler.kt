package com.teobaranga.monica.core.network

import com.skydoves.sandwich.ApiResponse
import io.ktor.client.HttpClient

interface ApiExceptionHandler {

    suspend fun <T> handle(
        response: ApiResponse.Failure.Exception,
        request: suspend HttpClient.() -> ApiResponse<T>,
    ): ApiResponse<T>
}
