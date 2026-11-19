package com.teobaranga.monica.contacts

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.contacts.list.Contacts
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
object ContactsRoute : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class ContactsRouteSerializer : NavKeySerializer by navKeySerializer(ContactsRoute.serializer())

fun EntryProviderScope<NavKey>.contacts() {
    entry<ContactsRoute> {
        Contacts()
    }
}
