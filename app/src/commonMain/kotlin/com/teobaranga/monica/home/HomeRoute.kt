package com.teobaranga.monica.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavGraphBuilder.home() {
    composable<HomeRoute> {
        Home()
    }
}
