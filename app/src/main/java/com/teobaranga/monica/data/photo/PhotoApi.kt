package com.teobaranga.monica.data.photo

import com.teobaranga.monica.data.contact.ContactPhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotoApi {

    @GET("api/contacts/{id}/photos")
    suspend fun getPhotos(@Path("id") id: Int): Response<ContactPhotosResponse>
}
