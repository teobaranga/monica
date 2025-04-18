package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.datetime.Instant

fun newContactEntity(
    contactId: Int,
    firstName: String,
    avatar: ContactEntity.Avatar = ContactEntity.Avatar(
        url = null,
        color = "#000000",
    ),
    lastName: String? = null,
    nickname: String? = null,
    birthdate: ContactEntity.Birthdate? = null,
    genderId: Int? = null,
    updated: Instant? = null,
    syncStatus: SyncStatus = SyncStatus.NEW,
): ContactEntity {
    return ContactEntity(
        contactId = contactId,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        completeName = buildString {
            append(firstName)
            if (lastName != null) {
                append(" ")
                append(lastName)
            }
            if (nickname != null) {
                append(" (")
                append(nickname)
                append(")")
            }
        },
        initials = buildString {
            append(firstName.take(1))
            if (lastName != null) {
                append(lastName.take(1))
            }
        },
        birthdate = birthdate,
        genderId = genderId,
        updated = updated,
        avatar = avatar,
        syncStatus = syncStatus,
    )
}
