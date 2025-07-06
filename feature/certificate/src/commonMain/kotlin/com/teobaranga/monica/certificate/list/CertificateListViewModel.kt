package com.teobaranga.monica.certificate.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.asitplus.signum.indispensable.asn1.Asn1Exception
import at.asitplus.signum.indispensable.asn1.encoding.decodeToString
import at.asitplus.signum.indispensable.pki.TbsCertificate
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.toStdlibInstant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(AppScope::class)
class CertificateListViewModel(
    certificateRepository: CertificateRepository,
) : ViewModel() {

    val certificatesListItem = certificateRepository.unsecureCertificates
        .map { certificates ->
            certificates.map {
                CertificateListItem(
                    sha256Hash = it.sha256,
                    issuer = it.tbsCertificate.getFirstIssuerName() ?: "Unknown issuer",
                    expiry = it.tbsCertificate.validUntil.instant.toStdlibInstant(),
                )
            }
        }
        .stateIn(
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
}
