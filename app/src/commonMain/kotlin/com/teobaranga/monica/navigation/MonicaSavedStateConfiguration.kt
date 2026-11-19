package com.teobaranga.monica.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teobaranga.monica.activity.nav.ContactActivityEditRoute
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.contact.nav.ContactDetailRoute
import com.teobaranga.monica.contact.nav.ContactEditRoute
import com.teobaranga.monica.contacts.ContactsRoute
import com.teobaranga.monica.dashboard.DashboardRoute
import com.teobaranga.monica.journal.list.JournalEntriesRoute
import com.teobaranga.monica.journal.view.JournalEntryRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Registers every navigation key so that the back stacks can be serialized on all platforms.
 *
 * Non-JVM targets cannot rely on reflection based serialization, so each [NavKey] has to be
 * registered explicitly. Any route missing from this list will crash when the state is saved.
 */
internal val MonicaSavedStateConfiguration: SavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ContactsRoute::class, ContactsRoute.serializer())
            subclass(DashboardRoute::class, DashboardRoute.serializer())
            subclass(ContactDetailRoute::class, ContactDetailRoute.serializer())
            subclass(ContactEditRoute::class, ContactEditRoute.serializer())
            subclass(ContactActivityEditRoute::class, ContactActivityEditRoute.serializer())
            subclass(JournalEntriesRoute::class, JournalEntriesRoute.serializer())
            subclass(JournalEntryRoute::class, JournalEntryRoute.serializer())
            subclass(CertificateListRoute::class, CertificateListRoute.serializer())
            subclass(CertificateDetailsRoute::class, CertificateDetailsRoute.serializer())
        }
    }
}
