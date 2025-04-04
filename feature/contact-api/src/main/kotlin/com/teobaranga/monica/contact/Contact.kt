package com.teobaranga.monica.contact

import com.teobaranga.monica.useravatar.UserAvatar
import java.time.OffsetDateTime

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val completeName: String,
    val initials: String,
    val avatar: UserAvatar,
    val updated: OffsetDateTime?,
)
