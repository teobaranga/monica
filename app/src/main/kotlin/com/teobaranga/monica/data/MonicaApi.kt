package com.teobaranga.monica.data

import androidx.annotation.Keep
import com.skydoves.sandwich.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

@Keep
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

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(@FieldMap tokenRequest: TokenRequest): ApiResponse<TokenResponse>
}
