package com.teobaranga.monica.data.contact

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactApi {

    @GET("api/contacts")
    suspend fun getContacts(): ApiResponse<MultipleContactsResponse>

    @GET("api/contacts/{id}")
    suspend fun getContact(@Path("id") id: Int): ApiResponse<SingleContactResponse>
}
