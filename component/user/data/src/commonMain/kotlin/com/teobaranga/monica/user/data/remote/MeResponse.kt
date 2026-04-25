package com.teobaranga.monica.user.data.remote

import com.teobaranga.monica.contact.data.remote.ContactResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class MeResponse(
    @SerialName("data")
    val data: Data,
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        @SerialName("email")
        val email: String,
        @SerialName("me_contact")
        val contact: ContactResponse?,
        @SerialName("updated_at")
        val updatedAt: Instant,
    )
}
