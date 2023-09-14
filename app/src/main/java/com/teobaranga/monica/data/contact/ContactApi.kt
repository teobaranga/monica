package com.teobaranga.monica.data.contact

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface ContactApi {

    @GET("api/contacts")
    suspend fun getContacts(): ApiResponse<ContactsResponse>
}
