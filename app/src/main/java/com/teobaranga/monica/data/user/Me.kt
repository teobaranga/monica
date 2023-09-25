package com.teobaranga.monica.data.user

import com.teobaranga.monica.contacts.model.Contact

data class Me(
    val firstName: String,
    val contact: Contact?,
)
