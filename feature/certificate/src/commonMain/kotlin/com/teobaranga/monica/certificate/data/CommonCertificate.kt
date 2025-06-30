package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.pki.X509Certificate
import kotlinx.io.bytestring.ByteString

// TODO: is this needed or can we just use X509Certificate directly?
data class CommonCertificate(
    val x509Certificate: X509Certificate,
    val sha256: ByteString,
    val sha1: ByteString,
)
