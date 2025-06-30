package com.teobaranga.monica.certificate.detail

import at.asitplus.signum.indispensable.CryptoPublicKey
import at.asitplus.signum.indispensable.X509SignatureAlgorithm
import at.asitplus.signum.indispensable.asn1.Asn1Time
import at.asitplus.signum.indispensable.asn1.KnownOIDs
import at.asitplus.signum.indispensable.asn1.authorityKeyIdentifier_2_5_29_35
import at.asitplus.signum.indispensable.asn1.clientAuth
import at.asitplus.signum.indispensable.asn1.codeSigning
import at.asitplus.signum.indispensable.asn1.emailProtection
import at.asitplus.signum.indispensable.asn1.encoding.decodeToString
import at.asitplus.signum.indispensable.asn1.extKeyUsage
import at.asitplus.signum.indispensable.asn1.ipsecEndSystem
import at.asitplus.signum.indispensable.asn1.ipsecTunnel
import at.asitplus.signum.indispensable.asn1.ipsecUser
import at.asitplus.signum.indispensable.asn1.keyUsage
import at.asitplus.signum.indispensable.asn1.ocspSigning
import at.asitplus.signum.indispensable.asn1.readOid
import at.asitplus.signum.indispensable.asn1.serverAuth
import at.asitplus.signum.indispensable.asn1.subjectKeyIdentifier
import at.asitplus.signum.indispensable.asn1.timeStamping
import at.asitplus.signum.indispensable.asn1.toBitSet
import at.asitplus.signum.indispensable.isSupported
import at.asitplus.signum.indispensable.pki.AlternativeNames
import at.asitplus.signum.indispensable.pki.AttributeTypeAndValue
import at.asitplus.signum.indispensable.pki.RelativeDistinguishedName
import at.asitplus.signum.indispensable.pki.TbsCertificate
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.certificate.util.hexFormatDisplay
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.io.bytestring.toHexString
import me.tatarka.inject.annotations.Inject

@Inject
class CertificateDetailsUiStateMapper {

    fun map(commonCertificate: CommonCertificate, isTrusted: Boolean): CertificateDetailsUiState {
        return CertificateDetailsUiState(
            isTrusted = isTrusted,
            sections = buildList {
                with(commonCertificate.x509Certificate.tbsCertificate) {
                    addSubjectNameSections(subjectName)
                    subjectAlternativeNames?.let {
                        addSubjectAltNamesSection(it)
                    }
                    addIssuerSections(issuerName)
                    addValiditySection(
                        validFrom = validFrom,
                        validUntil = validUntil,
                    )
                    decodedPublicKey.getOrNull()?.let { publicKey ->
                        addPublicKeyInfoSection(publicKey)
                    }
                    addMiscellaneousSection()
                    addFingerprintsSection(commonCertificate)
                    addKeyUsagesSection()
                    addExtendedKeyUsagesSection()
                    addSubjectKeyIdSection()
                    addAuthorityKeyIdSection()
                }
            }
        )
    }

    private fun MutableList<CertificateDetailsUiState.Section>.addSubjectNameSections(
        subjectNames: List<RelativeDistinguishedName>,
    ) {
        for (distinguishedName in subjectNames) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Subject",
                    items = buildList {
                        for (attributeTypeAndValue in distinguishedName.attrsAndValues) {
                            when (attributeTypeAndValue) {
                                is AttributeTypeAndValue.CommonName -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Common Name",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Country -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Country",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Organization -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Organization",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.OrganizationalUnit -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Organizational Unit",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Other -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Other",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }
                            }
                        }
                    }
                ),
            )
        }
    }

    private fun MutableList<CertificateDetailsUiState.Section>.addSubjectAltNamesSection(
        subjectAltNames: AlternativeNames,
    ) {
        add(
            CertificateDetailsUiState.Section(
                title = "Subject Alt Names",
                items = buildList {
                    subjectAltNames.dnsNames?.let { dnsNames ->
                        for (dnsName in dnsNames) {
                            add(
                                CertificateDetailsUiState.Section.Item(
                                    title = "DNS Name",
                                    value = dnsName,
                                )
                            )
                        }
                    }
                    // TODO: Add other types of subject alt names
                }
            )
        )
    }

    private fun MutableList<CertificateDetailsUiState.Section>.addIssuerSections(
        issuers: List<RelativeDistinguishedName>,
    ) {
        for (issuer in issuers) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Issuer",
                    items = buildList {
                        for (attributeTypeAndValue in issuer.attrsAndValues) {
                            when (attributeTypeAndValue) {
                                is AttributeTypeAndValue.CommonName -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Common Name",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString()
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Country -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Country",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Organization -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Organization",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.OrganizationalUnit -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Organizational Unit",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }

                                is AttributeTypeAndValue.Other -> {
                                    add(
                                        CertificateDetailsUiState.Section.Item(
                                            title = "Other",
                                            value = attributeTypeAndValue.value.asPrimitive()
                                                .decodeToString(),
                                        )
                                    )
                                }
                            }
                        }
                    },
                ),
            )
        }
    }

    private fun MutableList<CertificateDetailsUiState.Section>.addValiditySection(
        validFrom: Asn1Time,
        validUntil: Asn1Time,
    ) {
        add(
            CertificateDetailsUiState.Section(
                title = "Validity",
                items = listOf(
                    CertificateDetailsUiState.Section.Item(
                        title = "Not Before",
                        value = validFrom.instant.format(DateTimeComponents.Formats.RFC_1123),
                    ),
                    CertificateDetailsUiState.Section.Item(
                        title = "Not After",
                        value = validUntil.instant.format(DateTimeComponents.Formats.RFC_1123),
                    ),
                ),
            )
        )
    }

    private fun MutableList<CertificateDetailsUiState.Section>.addPublicKeyInfoSection(publicKey: CryptoPublicKey) {
        add(
            CertificateDetailsUiState.Section(
                title = "Public Key Info",
                items = listOf(
                    CertificateDetailsUiState.Section.Item(
                        title = "Algorithm",
                        value = when (publicKey) {
                            is CryptoPublicKey.EC -> "Elliptic Curve"
                            is CryptoPublicKey.RSA -> "RSA"
                        },
                    ),
                    CertificateDetailsUiState.Section.Item(
                        title = "Key Size",
                        value = when (val publicKey = publicKey) {
                            is CryptoPublicKey.EC -> publicKey.publicPoint.curve.coordinateLength.bits.toString()
                            is CryptoPublicKey.RSA -> publicKey.bits.number.toString()
                        },
                    ),
                    CertificateDetailsUiState.Section.Item(
                        title = "Public Value",
                        value = publicKey.iosEncoded.toHexString(hexFormatDisplay),
                    ),
                ),
            )
        )
    }

    context(tbsCertificate: TbsCertificate)
    private fun MutableList<CertificateDetailsUiState.Section>.addMiscellaneousSection() {
        add(
            CertificateDetailsUiState.Section(
                title = "Miscellaneous",
                items = buildList {
                    add(
                        CertificateDetailsUiState.Section.Item(
                            title = "Serial Number",
                            value = tbsCertificate.serialNumber.toHexString(hexFormatDisplay),
                        )
                    )
                    val algorithm = tbsCertificate.signatureAlgorithm
                    if (algorithm.isSupported()) {
                        add(
                            CertificateDetailsUiState.Section.Item(
                                title = "Signature Algorithm",
                                value = when (algorithm) {
                                    is X509SignatureAlgorithm.ECDSA -> {
                                        buildString {
                                            append("ECDSA")
                                            append(" with ${algorithm.digest}")
                                        }
                                    }

                                    is X509SignatureAlgorithm.RSAPKCS1 -> {
                                        buildString {
                                            append("RSA ${algorithm.digest}")
                                        }
                                    }

                                    is X509SignatureAlgorithm.RSAPSS -> {
                                        buildString {
                                            append("RSA ${algorithm.digest}")
                                        }
                                    }
                                },
                            )
                        )
                    }
                }
            )
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun MutableList<CertificateDetailsUiState.Section>.addFingerprintsSection(
        commonCertificate: CommonCertificate,
    ) {
        add(
            CertificateDetailsUiState.Section(
                title = "Fingerprints",
                items = listOf(
                    CertificateDetailsUiState.Section.Item(
                        title = "SHA-256",
                        value = commonCertificate.sha256.toHexString(hexFormatDisplay),
                    ),
                    CertificateDetailsUiState.Section.Item(
                        title = "SHA-1",
                        value = commonCertificate.sha1.toHexString(hexFormatDisplay),
                    ),
                ),
            )
        )
    }

    context(tbsCertificate: TbsCertificate)
    private fun MutableList<CertificateDetailsUiState.Section>.addKeyUsagesSection() {
        val keyUsages = getKeyUsages(tbsCertificate)
        if (keyUsages.isNotEmpty()) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Key Usages",
                    items = listOf(
                        CertificateDetailsUiState.Section.Item(
                            title = "Purposes",
                            value = keyUsages.joinToString(),
                        ),
                    )
                )
            )
        }
    }

    context(tbsCertificate: TbsCertificate)
    private fun MutableList<CertificateDetailsUiState.Section>.addExtendedKeyUsagesSection() {
        val extendedKeyUsages = getExtendedKeyUsages(tbsCertificate)
        if (extendedKeyUsages.isNotEmpty()) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Extended Key Usages",
                    items = listOf(
                        CertificateDetailsUiState.Section.Item(
                            title = "Purposes",
                            value = extendedKeyUsages.joinToString(),
                        ),
                    ),
                )
            )
        }
    }

    context(tbsCertificate: TbsCertificate)
    private fun MutableList<CertificateDetailsUiState.Section>.addSubjectKeyIdSection() {
        val subjectKeyId = getSubjectKeyId(tbsCertificate)
        if (subjectKeyId != null) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Subject Key ID",
                    items = listOf(
                        CertificateDetailsUiState.Section.Item(
                            title = "Key ID",
                            value = subjectKeyId.toHexString(hexFormatDisplay),
                        )
                    ),
                )
            )
        }
    }

    context(tbsCertificate: TbsCertificate)
    private fun MutableList<CertificateDetailsUiState.Section>.addAuthorityKeyIdSection() {
        val authorityKeyId = getAuthorityKeyId(tbsCertificate)
        if (authorityKeyId != null) {
            add(
                CertificateDetailsUiState.Section(
                    title = "Authority Key ID",
                    items = listOf(
                        CertificateDetailsUiState.Section.Item(
                            title = "Key ID",
                            value = authorityKeyId.toHexString(hexFormatDisplay),
                        )
                    ),
                )
            )
        }
    }

    private fun getKeyUsages(certificate: TbsCertificate): List<String> {
        val keyUsage = certificate.extensions?.firstOrNull { it.oid == KnownOIDs.keyUsage } ?: return emptyList()
        val keyUsages = keyUsage.value.asOctetString().content.toBitSet()
        return buildList {
            if (keyUsages[0]) add("Digital Signature")
            if (keyUsages[1]) add("Non-Repudiation")
            if (keyUsages[2]) add("Key Encipherment")
            if (keyUsages[3]) add("Data Encipherment")
            if (keyUsages[4]) add("Key Agreement")
            if (keyUsages[5]) add("Key Cert Sign")
            if (keyUsages[6]) add("CRL Sign")
            if (keyUsages[7]) add("Encipher Only")
            if (keyUsages[8]) add("Decipher Only")
        }
    }

    private fun getExtendedKeyUsages(certificate: TbsCertificate): List<String> {
        val extKeyUsage = certificate.extensions?.firstOrNull { it.oid == KnownOIDs.extKeyUsage } ?: return emptyList()
        val keyUsages = extKeyUsage.value.asEncapsulatingOctetString().children
            .first()
            .asSequence()
            .children
            .map { it.asPrimitive().readOid() }
            .toSet()
        return buildList {
            if (keyUsages.contains(KnownOIDs.serverAuth)) add("Server Authentication")
            if (keyUsages.contains(KnownOIDs.clientAuth)) add("Client Authentication")
            if (keyUsages.contains(KnownOIDs.codeSigning)) add("Code Signing")
            if (keyUsages.contains(KnownOIDs.emailProtection)) add("Email Protection")
            if (keyUsages.contains(KnownOIDs.ipsecEndSystem)) add("IPsec End System")
            if (keyUsages.contains(KnownOIDs.ipsecTunnel)) add("IPsec Tunnel")
            if (keyUsages.contains(KnownOIDs.ipsecUser)) add("IPsec User")
            if (keyUsages.contains(KnownOIDs.timeStamping)) add("Time Stamping")
            if (keyUsages.contains(KnownOIDs.ocspSigning)) add("OCSP Signing")
        }
    }

    private fun getSubjectKeyId(certificate: TbsCertificate): ByteArray? {
        val subjectKeyId =
            certificate.extensions?.firstOrNull { it.oid == KnownOIDs.subjectKeyIdentifier } ?: return null
        return subjectKeyId.value.asEncapsulatingOctetString().children
            .first()
            .asOctetString()
            .content
    }

    private fun getAuthorityKeyId(certificate: TbsCertificate): ByteArray? {
        val authorityKeyId =
            certificate.extensions?.firstOrNull { it.oid == KnownOIDs.authorityKeyIdentifier_2_5_29_35 }
                ?: return null
        return authorityKeyId.value.asEncapsulatingOctetString().children
            .first()
            .asSequence()
            .children
            .first()
            .asPrimitive()
            .content
    }
}
