package com.teobaranga.monica.certificate.list

import kotlin.time.Instant

data class CertificateListItem(
    val sha256Hash: String,
    val issuer: String,
    val expiry: Instant,
)
