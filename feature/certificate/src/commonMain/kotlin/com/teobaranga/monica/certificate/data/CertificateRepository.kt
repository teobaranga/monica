package com.teobaranga.monica.certificate.data

import com.diamondedge.logging.logging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class CertificateRepository(
    private val userTrustedCertificateDataStore: UserTrustedCertificateDataStore,
) {

    private val _unsecureCertificates = MutableStateFlow(emptyList<CommonCertificate>())
    val unsecureCertificates = _unsecureCertificates.asStateFlow()

    val userTrustedCertificates
        get() = MutableStateFlow(userTrustedCertificateDataStore.getUserTrustedCertificates())

    val allCertificates
        get() = combine(unsecureCertificates, userTrustedCertificates) { unsecure, userTrusted ->
            unsecure + userTrusted
        }

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
