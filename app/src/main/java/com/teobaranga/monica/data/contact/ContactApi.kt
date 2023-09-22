package com.teobaranga.monica.data.contact

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContactApi {

    @GET("api/contacts")
    suspend fun getContacts(
        @Query("sort") sort: String? = null,
    ): ApiResponse<MultipleContactsResponse>

    @GET("api/contacts/{id}")
    suspend fun getContact(@Path("id") id: Int): ApiResponse<SingleContactResponse>
}
