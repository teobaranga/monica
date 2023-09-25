package com.teobaranga.monica.data.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountResponse(
    @Json(name = "id")
    val id: Int,
)
