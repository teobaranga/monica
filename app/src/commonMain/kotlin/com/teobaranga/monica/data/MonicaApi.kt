package com.teobaranga.monica.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.apiResponseOf
import com.teobaranga.monica.core.network.HttpRequestMaker
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

const val PARAM_CLIENT_ID = "client_id"
const val PARAM_CLIENT_SECRET = "client_secret"
const val PARAM_REDIRECT_URI = "redirect_uri"
const val PARAM_CODE = "code"
const val PARAM_RESPONSE_TYPE = "response_type"
const val PARAM_GRANT_TYPE = "grant_type"

const val REDIRECT_URI = "https://monica.teobaranga.com/callback"

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)

class TokenRequest(
    clientId: String,
    clientSecret: String,
    code: String,
) : Map<String, String> by tokenRequestFields(clientId, clientSecret, code) {
    companion object {
        fun tokenRequestFields(clientId: String, clientSecret: String, code: String): Map<String, String> {
            return mapOf(
                PARAM_GRANT_TYPE to "authorization_code",
                PARAM_CLIENT_ID to clientId,
                PARAM_CLIENT_SECRET to clientSecret,
                PARAM_REDIRECT_URI to REDIRECT_URI,
                PARAM_CODE to code,
            )
        }
    }
}

interface MonicaApi {
    suspend fun getAccessToken(tokenRequest: TokenRequest): ApiResponse<TokenResponse>
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class MonicaApiImpl(private val httpRequestMaker: HttpRequestMaker) : MonicaApi {

    override suspend fun getAccessToken(tokenRequest: TokenRequest): ApiResponse<TokenResponse> {
        return httpRequestMaker.call {
            apiResponseOf {
                submitForm(
                    url = "oauth/token",
                    formParameters = parameters {
                        tokenRequest.forEach { (key, value) ->
                            append(key, value)
                        }
                    },
                )
            }
        }
    }
}
