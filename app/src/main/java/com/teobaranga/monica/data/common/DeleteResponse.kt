package com.teobaranga.monica.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteResponse(
    @SerialName("deleted")
    val deleted: Boolean,
    @SerialName("id")
    val id: Int,
)
