package com.teobaranga.monica.contact.nav

import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
data class ContactEditRoute(
    val contactId: Int? = null,
    val contactName: String? = null,
) : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class ContactEditRouteSerializer : NavKeySerializer by navKeySerializer(ContactEditRoute.serializer())
