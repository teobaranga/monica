package com.teobaranga.monica.data.user

import java.nio.ByteBuffer

data class Me(
    val firstName: String,
    val contact: Contact?,
) {
    data class Contact(
        val initials: String,
        val avatarColor: String,
        val avatarData: ByteBuffer?,
    )
}
