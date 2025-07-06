package com.teobaranga.monica.certificate.data

import com.diamondedge.logging.logging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class CertificateRepository {

    private val _unsecureCertificates = MutableStateFlow(emptyList<CommonCertificate>())
    val unsecureCertificates = _unsecureCertificates.asStateFlow()

    fun setUnsecureCertificates(certificates: List<CommonCertificate>) {
        _unsecureCertificates.value = certificates
        log.w { "Unsecure certificates: $certificates" }
    }

    fun clearUnsecureCertificates() {
        _unsecureCertificates.value = emptyList()
    }

    companion object {
        private val log = logging()
    }
}
