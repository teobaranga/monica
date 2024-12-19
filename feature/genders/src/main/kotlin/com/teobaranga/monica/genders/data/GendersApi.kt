package com.teobaranga.monica.genders.data

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface GendersApi {

    @GET("api/genders")
    suspend fun getGenders(): ApiResponse<GendersResponse>
}
