package com.teobaranga.monica.data.user

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface UserApi {

    @GET("api/me")
    suspend fun getMe(): ApiResponse<MeResponse>
}
