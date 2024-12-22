package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import com.teobaranga.monica.auth.AuthorizationRepository
import com.teobaranga.monica.inject.runtime.ContributesViewModel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class HomeViewModel internal constructor(
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
