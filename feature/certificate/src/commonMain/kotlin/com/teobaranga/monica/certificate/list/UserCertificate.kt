package com.teobaranga.monica.certificate.list

import kotlin.time.Instant

data class UserCertificate(
    val sha256Hash: String,
    val issuer: String,
    val expiry: Instant,
)
