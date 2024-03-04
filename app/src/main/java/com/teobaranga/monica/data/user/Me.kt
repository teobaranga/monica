package com.teobaranga.monica.data.user

import com.teobaranga.monica.contacts.list.model.Contact

data class Me(
    val firstName: String,
    val contact: Contact?,
)
