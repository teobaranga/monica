package com.teobaranga.monica.contacts.list.model

import com.teobaranga.monica.ui.avatar.UserAvatar
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
