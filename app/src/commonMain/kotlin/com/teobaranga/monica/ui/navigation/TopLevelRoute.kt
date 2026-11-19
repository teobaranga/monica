package com.teobaranga.monica.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

data class TopLevelRoute(
    val name: String,
    val route: NavKey,
    val icon: ImageVector,
)
