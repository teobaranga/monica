package com.teobaranga.monica.ui.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigator = compositionLocalOf<NavHostController> {
    error("No navigator provided")
}
