package com.teobaranga.monica.core.ui.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigator = compositionLocalOf<NavHostController> {
    error("No navigator provided")
}
