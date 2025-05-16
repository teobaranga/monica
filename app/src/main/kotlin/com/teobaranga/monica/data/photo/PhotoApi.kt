package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.contacts.data.ContactPhotosResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class PhotoApi(private val httpClient: () -> HttpClient) {

    suspend fun getPhotos(page: Int? = null): ApiResponse<ContactPhotosResponse> {
        return httpClient().getApiResponse("api/photos") {
            parameter("page", page)
        }
    }

    suspend fun getPhotos(id: Int): ApiResponse<ContactPhotosResponse> {
        return httpClient().getApiResponse("api/contacts/$id/photos")
    }
}
