package com.teobaranga.monica.inject.runtime

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider

interface ViewModelFactoryOwner {

    val viewModelFactory: ViewModelProvider.Factory
}

val LocalViewModelFactoryOwner = staticCompositionLocalOf<ViewModelFactoryOwner> {
    error("No ViewModelFactoryOwner provided")
}
