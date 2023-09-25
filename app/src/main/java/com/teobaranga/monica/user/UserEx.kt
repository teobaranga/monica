package com.teobaranga.monica.user

import com.teobaranga.monica.data.user.Me
import com.teobaranga.monica.ui.avatar.UserAvatar

val Me.userAvatar: UserAvatar
    get() = UserAvatar(
        contactId = -1,
        initials = firstName.take(2).uppercase(),
        color = "#709512",
        avatarUrl = null,
    )
