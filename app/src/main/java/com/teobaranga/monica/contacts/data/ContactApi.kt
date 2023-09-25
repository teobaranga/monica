package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContactApi {

    @GET("api/contacts")
    suspend fun getContacts(
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null,
    ): ApiResponse<MultipleContactsResponse>

    @GET("api/contacts/{id}")
    suspend fun getContact(@Path("id") id: Int): ApiResponse<SingleContactResponse>
}
