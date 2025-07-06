package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.pki.TbsCertificate

data class CommonCertificate(
    val tbsCertificate: TbsCertificate,
    val sha256: String,
    val sha1: String,
)
