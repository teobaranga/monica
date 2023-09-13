package com.teobaranga.monica.data.contact

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val initials: String,
    val avatar: Avatar,
) {
    data class Avatar(
        val url: String?,
        val color: String,
    )
}
