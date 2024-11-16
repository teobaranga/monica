package com.teobaranga.monica.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthorizationRepository : AuthorizationRepository {

    private var _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    fun logIn() {
        _isLoggedIn.value = true
    }

    fun logOut() {
        _isLoggedIn.value = false
    }
}
