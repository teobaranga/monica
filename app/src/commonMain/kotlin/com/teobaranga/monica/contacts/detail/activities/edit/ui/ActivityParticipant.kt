package com.teobaranga.monica.contacts.detail.activities.edit.ui

import com.teobaranga.monica.useravatar.UserAvatar

sealed interface ActivityParticipant {

    data class New(val name: String) : ActivityParticipant

    data class Contact(
        val contactId: Int,
        val name: String,
        val avatar: UserAvatar,
    ) : ActivityParticipant
}
