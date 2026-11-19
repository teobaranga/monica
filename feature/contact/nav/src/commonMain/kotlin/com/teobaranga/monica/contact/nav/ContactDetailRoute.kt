package com.teobaranga.monica.contact.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ContactDetailRoute(val contactId: Int) : NavKey
