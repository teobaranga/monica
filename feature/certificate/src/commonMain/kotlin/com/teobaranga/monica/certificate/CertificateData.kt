package com.teobaranga.monica.certificate

import kotlin.time.Instant

data class CertificateData(
    val subjectName: SubjectName?,
    val issuerName: IssuerName,
    val validity: Validity,
    val publicKeyInfo: PublicKeyInfo,
    val miscellaneous: Miscellaneous,
    val fingerprints: Fingerprints,
    val basicConstraints: BasicConstraints,
    val keyUsages: KeyUsages,
)

data class SubjectName(
    val commonName: String,
)

data class IssuerName(
    val commonName: String,
)

data class Validity(
    val notBefore: Instant,
    val notAfter: Instant,
)

data class PublicKeyInfo(
    val algorithm: String,
    val keySize: Int,
    val publicValue: String,
)

data class Miscellaneous(
    val serialNumber: String,
    val signatureAlgorithm: String,
    val version: Int,
)

data class Fingerprints(
    val sha256: String,
    val sha1: String,
)

data class BasicConstraints(
    val certificateAuthority: Boolean,
)

data class KeyUsages(
    val purposes: List<String>,
)
