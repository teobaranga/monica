package com.teobaranga.monica.setup

sealed interface SetupEvent {

    data class Login(
        val setupUrl: String,
        val isSecure: Boolean,
    ) : SetupEvent
}
