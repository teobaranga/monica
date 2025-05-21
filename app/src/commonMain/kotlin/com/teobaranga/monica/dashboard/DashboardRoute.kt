package com.teobaranga.monica.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

fun NavGraphBuilder.dashboard() {
    composable<DashboardRoute> {
        Dashboard()
    }
}
