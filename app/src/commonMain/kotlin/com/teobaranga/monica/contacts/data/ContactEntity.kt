package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.contact.Contact
import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.contacts.list.userAvatar

fun ContactEntity.toExternalModel(): Contact {
    val lastName = lastName
    return Contact(
        id = contactId,
        firstName = firstName,
        lastName = lastName,
        completeName = completeName,
        initials = when {
            initials.length >= 2 -> {
                initials
            }
            !lastName.isNullOrBlank() -> {
                firstName.take(1) + lastName.take(1)
            }
            else -> {
                firstName.take(2)
            }
        },
        avatar = userAvatar,
        updated = updated,
    )
}
