package com.teobaranga.monica.contact

import com.teobaranga.monica.useravatar.UserAvatar
import kotlin.time.Instant

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val completeName: String,
    val initials: String,
    val avatar: UserAvatar,
    val updated: Instant?,
)
