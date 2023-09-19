package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import com.teobaranga.monica.auth.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authorizationRepository: AuthorizationRepository,
    homeNavigationManager: HomeNavigationManager,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn

    val navigation = homeNavigationManager.navigation
}
