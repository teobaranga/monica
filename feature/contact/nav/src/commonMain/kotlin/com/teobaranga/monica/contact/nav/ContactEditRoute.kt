package com.teobaranga.monica.contact.nav

import kotlinx.serialization.Serializable

@Serializable
data class ContactEditRoute(
    val contactId: Int? = null,
    val contactName: String? = null,
)
