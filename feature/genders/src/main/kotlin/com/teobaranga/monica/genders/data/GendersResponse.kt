package com.teobaranga.monica.genders.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class GendersResponse(
    @Json(name = "data")
    val data: List<Gender>,
) {
    @JsonClass(generateAdapter = true)
    data class Gender(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,
    )
}
