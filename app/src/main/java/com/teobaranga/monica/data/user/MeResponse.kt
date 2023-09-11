package com.teobaranga.monica.data.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class MeResponse(
    @Json(name = "data")
    val data: Data,
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "first_name")
        val firstName: String,
        @Json(name = "updated_at")
        val updatedAt: Date,
    )
}
