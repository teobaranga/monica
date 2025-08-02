package com.teobaranga.monica.certificate.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import at.asitplus.signum.indispensable.asn1.Asn1Exception
import at.asitplus.signum.indispensable.asn1.encoding.decodeToString
import at.asitplus.signum.indispensable.pki.TbsCertificate
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Inject
@ContributesViewModel(scope = AppScope::class, assistedFactory = CertificateListViewModel.Factory::class)
class CertificateListViewModel(
    @Assisted
    savedStateHandle: SavedStateHandle,
    certificateRepository: CertificateRepository,
    private val now: () -> Instant,
) : ViewModel() {

    val route = savedStateHandle.toRoute<CertificateListRoute>()

    val certificatesListItem = when (route.certificateTrustStatus) {
        CertificateTrustStatus.UNTRUSTED -> certificateRepository.untrustedCertificates
        CertificateTrustStatus.TRUSTED -> certificateRepository.userTrustedCertificates
    }.map { certificates ->
        certificates.map {
            val tbsCertificate = it.x509Certificate.tbsCertificate
            val expiry = tbsCertificate.validUntil.instant
            CertificateListItem(
                sha256Hash = it.sha256,
                issuer = tbsCertificate.getFirstIssuerName() ?: "Unknown issuer",
                expiry = expiry,
                isExpired = expiry < now(),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = emptyList(),
    )

    private fun TbsCertificate.getFirstIssuerName(): String? {
        var name: String? = null

        for (relativeDistinguishedName in issuerName) {
            for (attributeTypeAndValue in relativeDistinguishedName.attrsAndValues) {
                try {
                    name = attributeTypeAndValue.value.asPrimitive()
                        .decodeToString()
                    break
                } catch (_: Asn1Exception) {
                    // Nothing to do
                }
            }
        }

        return name
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): CertificateListViewModel
    }
}
