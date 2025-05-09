package com.teobaranga.monica.activities.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateActivityResponse(
    @SerialName("data")
    val data: ContactActivitiesResponse.ContactActivity,
)
