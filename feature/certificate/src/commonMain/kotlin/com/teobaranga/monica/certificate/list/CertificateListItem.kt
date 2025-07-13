package com.teobaranga.monica.certificate.list

import kotlinx.io.bytestring.ByteString
import kotlin.time.Instant

data class CertificateListItem(
    val sha256Hash: ByteString,
    val issuer: String,
    val expiry: Instant,
    val isExpired: Boolean,
)
