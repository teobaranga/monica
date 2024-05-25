package com.teobaranga.monica.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class CreateActivityRequest(
    @Json(name = "activity_type_id")
    val activityTypeId: Int?,
    @Json(name = "summary")
    val summary: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "happened_at")
    val date: LocalDate,
    @Json(name = "contacts")
    val contacts: List<Int>,
    @Json(name = "emotions")
    val emotions: List<Int>?,
)
