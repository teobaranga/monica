package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.auth.AuthorizationRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.genders.data.GenderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    dispatcher: Dispatcher,
    authorizationRepository: AuthorizationRepository,
    homeNavigationManager: HomeNavigationManager,
    private val genderRepository: GenderRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn

    val navigation = homeNavigationManager.navigation

    init {
        // TODO: may not be the best place to load data
        viewModelScope.launch(dispatcher.io) {
            genderRepository.fetchLatestGenders()
        }
    }
}
