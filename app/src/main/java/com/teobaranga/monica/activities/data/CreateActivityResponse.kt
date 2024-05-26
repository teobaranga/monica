package com.teobaranga.monica.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateActivityResponse(
    @Json(name = "data")
    val data: ContactActivitiesResponse.ContactActivity,
)
