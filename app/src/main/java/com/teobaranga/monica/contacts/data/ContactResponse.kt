package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.data.adapter.OffsetDateTimeAsString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("complete_name")
    val completeName: String,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("initials")
    val initials: String,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("information")
    val info: Information,
    @SerialName("updated_at")
    val updated: OffsetDateTimeAsString? = null,
) {
    @Serializable
    data class Information(
        @SerialName("avatar")
        val avatar: Avatar,
        @SerialName("dates")
        val dates: Dates? = null,
    ) {
        @Serializable
        data class Avatar(
            @SerialName("url")
            val url: String?,
            @SerialName("default_avatar_color")
            val color: String,
        )

        @Serializable
        data class Dates(
            @SerialName("birthdate")
            val birthdate: Birthdate,
        ) {
            @Serializable
            data class Birthdate(
                @SerialName("is_age_based")
                val isAgeBased: Boolean?,
                @SerialName("is_year_unknown")
                val isYearUnknown: Boolean?,
                @SerialName("date")
                val date: OffsetDateTimeAsString?,
            )
        }
    }
}
