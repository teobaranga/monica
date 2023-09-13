package com.teobaranga.monica.contacts

data class ContactsUiState(
    val contacts: List<Contact>,
) {
    data class Contact(
        val id: Int,
        val name: String,
    )
}
