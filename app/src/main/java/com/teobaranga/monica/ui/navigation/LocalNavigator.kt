package com.teobaranga.monica.ui.navigation

import androidx.compose.runtime.compositionLocalOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

val LocalDestinationsNavigator = compositionLocalOf<DestinationsNavigator> {
    error("No navigator provided")
}
