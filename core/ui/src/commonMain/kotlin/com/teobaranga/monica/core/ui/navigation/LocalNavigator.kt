package com.teobaranga.monica.core.ui.navigation

import androidx.compose.runtime.compositionLocalOf

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No navigator provided")
}
