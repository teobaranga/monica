package com.teobaranga.monica.contacts

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.contacts.list.Contacts
import kotlinx.serialization.Serializable

@Serializable
object ContactsRoute : NavKey

fun EntryProviderScope<NavKey>.contacts() {
    entry<ContactsRoute> {
        Contacts()
    }
}
