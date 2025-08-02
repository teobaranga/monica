package com.teobaranga.monica.setup.domain

sealed interface SignInResult {

    data object Success : SignInResult

    sealed interface Error : SignInResult {

        data object UnknownError : Error
    }
}
