package com.teobaranga.monica.contacts.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class ContactResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "complete_name")
    val completeName: String,
    @Json(name = "nickname")
    val nickname: String?,
    @Json(name = "initials")
    val initials: String,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "gender_type")
    val genderId: String?,
    @Json(name = "information")
    val info: Information,
    @Json(name = "updated_at")
    val updated: ZonedDateTime?,
) {
    @JsonClass(generateAdapter = true)
    data class Information(
        @Json(name = "avatar")
        val avatar: Avatar,
        @Json(name = "dates")
        val dates: Dates?,
    ) {
        @JsonClass(generateAdapter = true)
        data class Avatar(
            @Json(name = "url")
            val url: String?,
            @Json(name = "default_avatar_color")
            val color: String,
        )

        @JsonClass(generateAdapter = true)
        data class Dates(
            @Json(name = "birthdate")
            val birthdate: Birthdate,
        ) {
            @JsonClass(generateAdapter = true)
            data class Birthdate(
                @Json(name = "is_age_based")
                val isAgeBased: Boolean?,
                @Json(name = "is_year_unknown")
                val isYearUnknown: Boolean?,
                @Json(name = "date")
                val date: ZonedDateTime?,
            )
        }
    }
}
