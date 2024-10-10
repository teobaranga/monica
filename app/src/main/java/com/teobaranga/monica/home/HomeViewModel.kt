package com.teobaranga.monica.home

import androidx.lifecycle.ViewModel
import com.teobaranga.monica.auth.AuthorizationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn
}
