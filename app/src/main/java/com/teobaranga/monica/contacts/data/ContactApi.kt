package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.activities.data.ContactActivitiesResponse
import com.teobaranga.monica.activities.data.CreateActivityRequest
import com.teobaranga.monica.activities.data.CreateActivityResponse
import com.teobaranga.monica.activities.data.CreateContactRequest
import com.teobaranga.monica.data.common.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("api/contacts")
    suspend fun createContact(@Body request: CreateContactRequest): ApiResponse<SingleContactResponse>

    @PUT("api/contacts/{id}")
    suspend fun updateContact(
        @Path("id") id: Int,
        @Body request: CreateContactRequest,
    ): ApiResponse<SingleContactResponse>

    @GET("api/contacts/{id}/activities")
    suspend fun getContactActivities(
        @Path("id") id: Int,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
    ): ApiResponse<ContactActivitiesResponse>

    @POST("api/activities")
    suspend fun createActivity(@Body request: CreateActivityRequest): ApiResponse<CreateActivityResponse>

    @PUT("api/activities/{id}")
    suspend fun updateActivity(
        @Path("id") id: Int,
        @Body request: CreateActivityRequest,
    ): ApiResponse<CreateActivityResponse>

    @DELETE("api/activities/{id}")
    suspend fun deleteActivity(@Path("id") id: Int): ApiResponse<DeleteResponse>
}
