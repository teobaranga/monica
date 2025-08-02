package com.teobaranga.monica.certificate.list

import okio.ByteString
import kotlin.time.Instant

data class CertificateListItem(
    val sha256Hash: ByteString,
    val issuer: String,
    val expiry: Instant,
    val isExpired: Boolean,
)
