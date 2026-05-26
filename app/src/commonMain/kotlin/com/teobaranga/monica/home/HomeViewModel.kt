package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
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
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
