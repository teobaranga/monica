package com.teobaranga.monica.data.user

import com.teobaranga.monica.contacts.data.ContactResponse
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeResponse(
    @SerialName("data")
    val data: Data,
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: Int,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("me_contact")
        val contact: ContactResponse?,
        @SerialName("updated_at")
        val updatedAt: Instant,
    )
}
