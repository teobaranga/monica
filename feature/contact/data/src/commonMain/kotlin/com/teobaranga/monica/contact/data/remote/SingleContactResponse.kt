package com.teobaranga.monica.contact.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleContactResponse(
    @SerialName("data")
    val data: ContactResponse,
)
