package com.teobaranga.monica.contact.nav

import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
data class ContactDetailRoute(val contactId: Int) : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class ContactDetailRouteSerializer : NavKeySerializer by navKeySerializer(ContactDetailRoute.serializer())
