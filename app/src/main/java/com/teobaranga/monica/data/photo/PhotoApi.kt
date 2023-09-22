package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.contact.ContactPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotoApi {

    @GET("api/photos")
    suspend fun getPhotos(): ApiResponse<ContactPhotosResponse>

    @GET("api/contacts/{id}/photos")
    suspend fun getPhotos(@Path("id") id: Int): ApiResponse<ContactPhotosResponse>
}
