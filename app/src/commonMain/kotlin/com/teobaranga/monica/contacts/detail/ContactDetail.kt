package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection

@Immutable
data class ContactDetail(
    val id: Int,
    val fullName: String,
    val infoSections: List<ContactInfoSection>,
)
