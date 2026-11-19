package com.teobaranga.monica.dashboard

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute : NavKey

fun EntryProviderScope<NavKey>.dashboard() {
    entry<DashboardRoute> {
        Dashboard()
    }
}
