package com.teobaranga.monica.setup

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SetupRoute

fun NavGraphBuilder.setup() {
    composable<SetupRoute> {
        Setup()
    }
}
