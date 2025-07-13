package com.teobaranga.monica.certificate.data

sealed interface UntrustedCertificateResult {
    data object Accepted : UntrustedCertificateResult
    data object Refused : UntrustedCertificateResult
}
