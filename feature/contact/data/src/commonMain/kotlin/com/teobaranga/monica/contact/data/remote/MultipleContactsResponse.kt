package com.teobaranga.monica.contact.data.remote

import com.teobaranga.monica.core.data.remote.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultipleContactsResponse(
    @SerialName("data")
    val data: List<ContactResponse>,
    @SerialName("meta")
    val meta: MetaResponse,
)
