package com.teobaranga.monica.contacts.detail.activities.edit.domain

import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.contact.userAvatar
import com.teobaranga.monica.contacts.detail.activities.edit.ui.ActivityParticipant
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
