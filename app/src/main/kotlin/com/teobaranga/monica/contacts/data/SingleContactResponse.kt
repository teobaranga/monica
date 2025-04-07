package com.teobaranga.monica.contacts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleContactResponse(
    @SerialName("data")
    val data: ContactResponse,
)
