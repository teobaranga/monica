package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.ui.avatar.UserAvatar

@Immutable
data class ContactDetail(
    val fullName: String,
    val userAvatar: UserAvatar,
    val tabs: List<String>,
)
