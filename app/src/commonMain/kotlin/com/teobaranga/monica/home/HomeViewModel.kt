package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.auth.AuthorizationRepository
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class HomeViewModel(
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
