package com.teobaranga.monica.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val ERROR_CODE_DATA_UNAVAILABLE = 32

@Serializable
data class ErrorResponse(
    val error: Error,
) {
    @Serializable
    data class Error(
        @SerialName("message")
        val message: List<String>,
        @SerialName("error_code")
        val errorCode: Int,
    )
}
