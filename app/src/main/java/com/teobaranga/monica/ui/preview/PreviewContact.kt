package com.teobaranga.monica.ui.preview

import com.teobaranga.monica.contact.Contact
import com.teobaranga.monica.useravatar.UserAvatar

internal val contactAlice = Contact(
    id = 1,
    firstName = "Alice",
    lastName = null,
    completeName = "Alice",
    initials = "A",
    avatar = UserAvatar(
        contactId = 1,
        initials = "A",
        color = "#FF0000",
        avatarUrl = null,
    ),
    updated = null,
)

internal val contactBob = Contact(
    id = 2,
    firstName = "Bob",
    lastName = null,
    completeName = "Bob",
    initials = "B",
    avatar = UserAvatar(
        contactId = 2,
        initials = "B",
        color = "#00FF00",
        avatarUrl = null,
    ),
    updated = null,
)
