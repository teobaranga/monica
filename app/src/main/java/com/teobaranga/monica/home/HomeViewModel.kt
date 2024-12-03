package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import com.teobaranga.monica.auth.AuthorizationRepository

@me.tatarka.inject.annotations.Inject
internal class HomeViewModel(
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
