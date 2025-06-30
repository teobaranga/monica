package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.pki.X509Certificate
import kotlinx.io.bytestring.ByteString

data class CommonCertificate(
    val x509Certificate: X509Certificate,
    val sha256: ByteString,
    val sha1: ByteString,
)
