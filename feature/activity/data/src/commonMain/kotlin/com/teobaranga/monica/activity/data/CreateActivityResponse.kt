package com.teobaranga.monica.activity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateActivityResponse(
    @SerialName("data")
    val data: ContactActivitiesResponse.ContactActivity,
)
