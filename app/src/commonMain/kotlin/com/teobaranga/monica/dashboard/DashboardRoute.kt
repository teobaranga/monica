package com.teobaranga.monica.dashboard

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class DashboardRouteSerializer : NavKeySerializer by navKeySerializer(DashboardRoute.serializer())

fun EntryProviderScope<NavKey>.dashboard() {
    entry<DashboardRoute> {
        Dashboard()
    }
}
