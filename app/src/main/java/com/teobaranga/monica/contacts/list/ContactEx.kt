package com.teobaranga.monica.contacts.list

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.ui.avatar.UserAvatar

val Contact.userAvatar: UserAvatar
    get() = UserAvatar(
        contactId = id,
        initials = initials,
        color = avatarColor,
        avatarUrl = avatarUrl,
    )

val ContactEntity.userAvatar: UserAvatar
    get() = UserAvatar(
        contactId = contactId,
        initials = initials,
        color = avatarColor,
        avatarUrl = avatarUrl,
    )
