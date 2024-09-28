package com.teobaranga.monica.ui

import androidx.compose.runtime.compositionLocalOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

val LocalDestinationsNavigator = compositionLocalOf<DestinationsNavigator> {
    error("No navigator provided")
}
