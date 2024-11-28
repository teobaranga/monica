package com.teobaranga.monica.activities.data

import com.teobaranga.monica.data.adapter.LocalDateAsString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateActivityRequest(
    @SerialName("activity_type_id")
    val activityTypeId: Int?,
    @SerialName("summary")
    val summary: String,
    @SerialName("description")
    val description: String?,
    @SerialName("happened_at")
    val date: LocalDateAsString,
    @SerialName("contacts")
    val contacts: List<Int>,
    @SerialName("emotions")
    val emotions: List<Int>?,
)
