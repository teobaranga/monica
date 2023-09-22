package com.teobaranga.monica.data.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaResponse(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "last_page")
    val lastPage: Int,
)
