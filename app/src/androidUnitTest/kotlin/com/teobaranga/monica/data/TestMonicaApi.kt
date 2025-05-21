package com.teobaranga.monica.data

import com.skydoves.sandwich.ApiResponse
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(
    scope = AppScope::class,
    replaces = [MonicaApiImpl::class],
)
class TestMonicaApi : MonicaApi {

    override suspend fun getAccessToken(tokenRequest: TokenRequest): ApiResponse<TokenResponse> {
        return ApiResponse.of {
            TokenResponse(
                accessToken = "access_token",
                refreshToken = "refresh_token",
            )
        }
    }
}
