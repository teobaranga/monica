package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teobaranga.monica.auth.AuthorizationRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Inject
@ContributesIntoMap(AppScope::class)
@ViewModelKey
class HomeViewModel(
    authorizationRepository: AuthorizationRepository,
    val savedStateConfiguration: SavedStateConfiguration,
    val entryProvider: (NavKey) -> NavEntry<NavKey>,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
