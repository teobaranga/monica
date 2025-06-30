package com.teobaranga.monica.certificate.data

import kotlinx.io.bytestring.ByteString

interface UserTrustedCertificateDataStore {

    fun getUserTrustedCertificates(): List<CommonCertificate>

    suspend fun trustCertificates(certificates: List<CommonCertificate>)

    suspend fun deleteCertificate(sha256Hash: ByteString)
}
