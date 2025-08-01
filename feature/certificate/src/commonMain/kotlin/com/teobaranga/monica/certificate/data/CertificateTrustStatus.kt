package com.teobaranga.monica.certificate.data

import kotlinx.serialization.Serializable

@Serializable
enum class CertificateTrustStatus {
    UNTRUSTED,
    TRUSTED,
}
