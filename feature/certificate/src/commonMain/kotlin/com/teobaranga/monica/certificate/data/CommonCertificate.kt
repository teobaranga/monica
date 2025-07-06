package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.pki.X509Certificate

data class CommonCertificate(
    val x509Certificate: X509Certificate,
    val sha256: String,
    val sha1: String,
)
