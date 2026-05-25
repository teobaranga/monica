package com.teobaranga.monica.certificate.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import at.asitplus.signum.indispensable.asn1.Asn1Exception
import at.asitplus.signum.indispensable.asn1.encoding.decodeToString
import at.asitplus.signum.indispensable.pki.TbsCertificate
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@AssistedInject
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
    @ViewModelAssistedFactoryKey(CertificateListViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory: ViewModelAssistedFactory {

        override fun create(extras: CreationExtras): ViewModel {
            return create(extras.createSavedStateHandle())
        }

        fun create(@Assisted savedStateHandle: SavedStateHandle): CertificateListViewModel
    }
}
