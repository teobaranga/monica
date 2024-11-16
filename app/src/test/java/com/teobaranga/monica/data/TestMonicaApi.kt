package com.teobaranga.monica.data

import com.skydoves.sandwich.ApiResponse
import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
abstract class ApiModule {

    @Binds
    abstract fun provideApi(api: TestMonicaApi): MonicaApi
}

class TestMonicaApi @Inject constructor() : MonicaApi {

    override suspend fun getAccessToken(tokenRequest: TokenRequest): ApiResponse<TokenResponse> {
        return ApiResponse.of {
            TokenResponse(
                accessToken = "access_token",
                refreshToken = "refresh_token",
            )
        }
    }
}
