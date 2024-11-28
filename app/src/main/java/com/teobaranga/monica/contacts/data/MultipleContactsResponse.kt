package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.data.common.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultipleContactsResponse(
    @SerialName("data")
    val data: List<ContactResponse>,
    @SerialName("meta")
    val meta: MetaResponse,
)
