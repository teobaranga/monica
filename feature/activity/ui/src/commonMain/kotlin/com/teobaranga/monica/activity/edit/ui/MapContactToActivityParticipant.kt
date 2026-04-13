package com.teobaranga.monica.activity.edit.ui

import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.contact.userAvatar
import me.tatarka.inject.annotations.Inject

@Inject
class MapContactToActivityParticipant {

    operator fun invoke(contact: ContactEntity): ActivityParticipant.Contact {
        return ActivityParticipant.Contact(
            contactId = contact.contactId,
            name = contact.completeName,
            avatar = contact.userAvatar,
        )
    }
}
