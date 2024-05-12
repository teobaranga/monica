package com.teobaranga.monica.contacts.list.model

import java.time.OffsetDateTime

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val completeName: String,
    val initials: String,
    val avatarUrl: String?,
    val avatarColor: String,
    val updated: OffsetDateTime?,
)
