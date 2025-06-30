package com.teobaranga.monica.setup.domain

interface SignInExceptionHandler {

    fun handle(exception: Throwable): SignInResult.Error
}
