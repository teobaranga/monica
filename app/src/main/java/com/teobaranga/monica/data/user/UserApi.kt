package com.teobaranga.monica.data.user

import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("api/me")
    suspend fun getMe(): Response<MeResponse>
}
