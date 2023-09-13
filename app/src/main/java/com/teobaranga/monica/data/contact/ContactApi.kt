package com.teobaranga.monica.data.contact

import retrofit2.Response
import retrofit2.http.GET

interface ContactApi {

    @GET("api/contacts")
    suspend fun getContacts(): Response<ContactsResponse>
}
