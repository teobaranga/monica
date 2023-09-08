package com.teobaranga.monica.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthorizationRepository : AuthorizationRepository {

    private var _isLoggedIn : Boolean? = null

    override val isLoggedIn: StateFlow<Boolean?>
        get() = MutableStateFlow(_isLoggedIn)

    fun logIn() {
        _isLoggedIn = true
    }

    fun logOut() {
        _isLoggedIn = false
    }

    override suspend fun signIn(clientId: String, clientSecret: String, authorizationCode: String): Boolean {
        return false
    }
}
