package com.teobaranga.monica.contacts.detail.activities.edit.domain

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.detail.activities.edit.ui.ActivityParticipant
import com.teobaranga.monica.contacts.list.userAvatar
import javax.inject.Inject

class MapContactToActivityParticipant @Inject constructor() {

    operator fun invoke(contact: ContactEntity): ActivityParticipant.Contact {
        return ActivityParticipant.Contact(
            contactId = contact.contactId,
            name = contact.completeName,
            avatar = contact.userAvatar,
        )
    }
}
