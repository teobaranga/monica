package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.core.data.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactPhotosResponse(
    @SerialName("data")
    val data: List<Photo>,
    @SerialName("meta")
    val meta: MetaResponse,
) {
    @Serializable
    data class Photo(
        @SerialName("id")
        val id: Int,
        @SerialName("new_filename")
        val fileName: String,
        @SerialName("dataUrl")
        val data: String,
        @SerialName("contact")
        val contact: ContactResponse,
    )
}
