package com.teobaranga.monica.core.network

import at.asitplus.signum.indispensable.pki.X509Certificate

interface CertificateTruster {

    suspend fun trustCertificates(certificates: List<X509Certificate>)
}
