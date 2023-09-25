package com.teobaranga.monica.contacts

import com.teobaranga.monica.contacts.model.Contact
import com.teobaranga.monica.ui.avatar.UserAvatar

val Contact.userAvatar: UserAvatar
    get() = UserAvatar(
        contactId = id,
        initials = initials,
        color = avatarColor,
        avatarUrl = avatarUrl,
    )
