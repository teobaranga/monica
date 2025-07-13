package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.contacts.data.ContactPhotosResponse
import com.teobaranga.monica.core.network.HttpRequestMaker
import io.ktor.client.request.parameter
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class PhotoApi(private val httpRequestMaker: HttpRequestMaker) {

    suspend fun getPhotos(page: Int? = null): ApiResponse<ContactPhotosResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/photos") {
                parameter("page", page)
            }
        }
    }

    suspend fun getPhotos(id: Int): ApiResponse<ContactPhotosResponse> {
        return httpRequestMaker.call {
            getApiResponse("api/contacts/$id/photos")
        }
    }
}
