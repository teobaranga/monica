package com.teobaranga.monica.inject.runtime

import androidx.lifecycle.ViewModelProvider

/**
 * Represents the owner of a ViewModel factory. Since there is generally only one such factory,
 * the Application class is usually the main implementation.
 */
interface ViewModelFactoryOwner {

    val viewModelFactory: ViewModelProvider.Factory
}
