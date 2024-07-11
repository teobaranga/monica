package com.teobaranga.monica.contacts.list

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.ui.avatar.UserAvatar

val ContactEntity.userAvatar: UserAvatar
    get() = UserAvatar(
        contactId = contactId,
        initials = initials,
        color = avatar.color,
        avatarUrl = avatar.url,
    )
