package com.teobaranga.monica.certificate

import androidx.lifecycle.ViewModel
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(scope = AppScope::class)
class CertificateViewModel(
    private val certificateRepository: CertificateRepository,
) : ViewModel() {

    val certificateDetails
        get() = certificateRepository.certificates.firstOrNull()
            ?.let {
                CertificateDetailsUiState(
                    sections = buildList {
                        if (it.subjectName != null) {
                            add(
                                CertificateDetailsUiState.Section(
                                    title = "Subject",
                                    items = listOf(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Common Name",
                                            value = it.subjectName.commonName,
                                        ),
                                    ),
                                ),
                            )
                        }
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Issuer",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Common Name",
                                        value = it.issuerName.commonName,
                                    ),
                                ),
                            ),
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Validity",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Not Before",
                                        value = it.validity.notBefore.toString(),
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Not After",
                                        value = it.validity.notAfter.toString(),
                                    ),
                                ),
                            )
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Public Key Info",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Algorithm",
                                        value = it.publicKeyInfo.algorithm,
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Key Size",
                                        value = it.publicKeyInfo.keySize.toString(),
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Public Value",
                                        value = it.publicKeyInfo.publicValue,
                                    ),
                                ),
                            )
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Miscellaneous",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Serial Number",
                                        value = it.miscellaneous.serialNumber,
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Signature Algorithm",
                                        value = it.miscellaneous.signatureAlgorithm,
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Version",
                                        value = it.miscellaneous.version.toString(),
                                    ),
                                ),
                            )
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Fingerprints",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "SHA-256",
                                        value = it.fingerprints.sha256,
                                    ),
                                    CertificateDetailsUiState.Section.Item(
                                        title = "SHA-1",
                                        value = it.fingerprints.sha1,
                                    ),
                                ),
                            )
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Basic Constraints",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "CA",
                                        value = it.basicConstraints.certificateAuthority.toString(),
                                    )
                                ),
                            )
                        )
                        add(
                            CertificateDetailsUiState.Section(
                                title = "Key Usages",
                                items = listOf(
                                    CertificateDetailsUiState.Section.Item(
                                        title = "Purposes",
                                        value = it.keyUsages.purposes.joinToString(),
                                    ),
                                )
                            )
                        )
                    }
                )
            }
}

data class CertificateDetailsUiState(
    val sections: List<Section>,
) {
    data class Section(
        val title: String,
        val items: List<Item>,
    ) {
        data class Item(
            val title: String,
            val value: String,
            val isCopyEnabled: Boolean = false,
        )
    }
}
