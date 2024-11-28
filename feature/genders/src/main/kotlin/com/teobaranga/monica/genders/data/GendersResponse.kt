package com.teobaranga.monica.genders.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GendersResponse(
    @SerialName("data")
    val data: List<Gender>,
) {
    @Serializable
    data class Gender(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
    )
}
