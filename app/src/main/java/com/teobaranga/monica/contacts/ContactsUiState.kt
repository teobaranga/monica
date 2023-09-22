package com.teobaranga.monica.contacts

import com.teobaranga.monica.ui.avatar.UserAvatar

data class ContactsUiState(
    val contacts: List<Contact>,
) {
    data class Contact(
        val id: Int,
        val name: String,
        val userAvatar: UserAvatar,
    )
}
