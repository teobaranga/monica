package com.teobaranga.monica.data.contact

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
    @Json(name = "initials")
    val initials: String,
    @Json(name = "information")
    val info: Information,
    @Json(name = "updated_at")
    val updated: ZonedDateTime?,
) {
    @JsonClass(generateAdapter = true)
    data class Information(
        @Json(name = "avatar")
        val avatar: Avatar,
    ) {
        @JsonClass(generateAdapter = true)
        data class Avatar(
            @Json(name = "url")
            val url: String?,
            @Json(name = "default_avatar_color")
            val color: String,
        )
    }
}
