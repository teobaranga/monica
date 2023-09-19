package com.teobaranga.monica.data.user

data class Me(
    val firstName: String,
    val contact: Contact?,
) {
    data class Contact(
        val id: Int,
        val initials: String,
        val avatarColor: String,
        val avatarUrl: String?,
    )
}
