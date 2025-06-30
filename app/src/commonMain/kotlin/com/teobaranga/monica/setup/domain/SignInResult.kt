package com.teobaranga.monica.setup.domain

import com.teobaranga.monica.certificate.CertificateData

sealed interface SignInResult {

    data object Success : SignInResult

    sealed interface Error : SignInResult {

        data class UntrustedCertificates(val certificates: List<CertificateData>) : Error

        data object UnknownError : Error
    }
}
