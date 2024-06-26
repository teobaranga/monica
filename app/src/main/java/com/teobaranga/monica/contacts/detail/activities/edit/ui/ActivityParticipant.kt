package com.teobaranga.monica.contacts.detail.activities.edit.ui

import com.teobaranga.monica.ui.avatar.UserAvatar

data class ActivityParticipant(
    val contactId: Int,
    val name: String,
    val avatar: UserAvatar,
)
