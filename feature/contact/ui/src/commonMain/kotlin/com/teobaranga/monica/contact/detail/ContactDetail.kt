package com.teobaranga.monica.contact.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contact.detail.section.ContactInfoSection

@Immutable
data class ContactDetail(
    val id: Int,
    val fullName: String,
    val infoSections: List<ContactInfoSection>,
)
