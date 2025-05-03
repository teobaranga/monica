package com.teobaranga.monica.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

const val ERROR_CODE_DATA_UNAVAILABLE = 32

@Serializable
data class ErrorResponse(
    @SerialName("error")
    val error: Error,
) {
    @Serializable
    data class Error(
        @SerialName("message")
        val message: JsonElement,
        @SerialName("error_code")
        val errorCode: Int,
    )
}
