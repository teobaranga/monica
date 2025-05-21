package com.teobaranga.monica.contacts.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class ContactDetailRoute(val contactId: Int)

fun NavGraphBuilder.contactDetail() {
    composable<ContactDetailRoute> {
        ContactDetail()
    }
}
