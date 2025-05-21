package com.teobaranga.monica.contacts

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.monica.contacts.list.Contacts
import kotlinx.serialization.Serializable

@Serializable
object ContactsRoute

fun NavGraphBuilder.contacts() {
    composable<ContactsRoute> {
        Contacts()
    }
}
