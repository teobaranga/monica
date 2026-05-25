package com.teobaranga.monica.network

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.core.network.ApiExceptionHandler
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI

@Inject
@ContributesBinding(scope = AppScope::class)
class IosApiExceptionHandler : ApiExceptionHandler {

    @OptIn(InternalAPI::class)
    override suspend fun <T> handle(
        response: ApiResponse.Failure.Exception,
        request: suspend HttpClient.() -> ApiResponse<T>,
    ): ApiResponse<T> {
        // TODO implement certificate exception handling similar to Android
        return response
    }
}
