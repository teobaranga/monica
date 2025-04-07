package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.data.ContactPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {

    @GET("api/photos")
    suspend fun getPhotos(@Query("page") page: Int? = null): ApiResponse<ContactPhotosResponse>

    @GET("api/contacts/{id}/photos")
    suspend fun getPhotos(@Path("id") id: Int): ApiResponse<ContactPhotosResponse>
}
