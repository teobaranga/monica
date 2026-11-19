package com.teobaranga.monica.activity.nav

import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
data class ContactActivityEditRoute(
    val contactId: Int?,
    val activityId: Int?,
) : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class ContactActivityEditRouteSerializer : NavKeySerializer by navKeySerializer(ContactActivityEditRoute.serializer())
