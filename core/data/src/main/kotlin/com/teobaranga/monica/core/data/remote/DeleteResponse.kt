package com.teobaranga.monica.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteResponse(
    @SerialName("deleted")
    val deleted: Boolean,
    @SerialName("id")
    val id: Int,
)
