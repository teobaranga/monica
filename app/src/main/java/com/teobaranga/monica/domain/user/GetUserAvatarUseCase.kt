package com.teobaranga.monica.domain.user

import com.teobaranga.monica.data.user.Me
import com.teobaranga.monica.ui.avatar.UserAvatar
import javax.inject.Inject

class GetUserAvatarUseCase @Inject constructor() {
    operator fun invoke(me: Me): UserAvatar {
        return if (me.contact != null) {
            UserAvatar(
                contactId = me.contact.id,
                initials = me.contact.initials,
                color = me.contact.avatarColor,
                avatarUrl = me.contact.avatarUrl,
            )
        } else {
            UserAvatar(
                contactId = -1,
                initials = me.firstName.take(2).uppercase(),
                color = "#709512",
                avatarUrl = null,
            )
        }
    }
}
