package com.teobaranga.monica.data.contact

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contact(
    @Json(name = "initials")
    val initials: String,
    @Json(name = "information")
    val info: Information,
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
