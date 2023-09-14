package com.teobaranga.monica.data.contact

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleContactResponse(
    @Json(name = "data")
    val data: ContactResponse,
)
