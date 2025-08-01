package com.teobaranga.monica.certificate.data

import okio.ByteString

interface UserTrustedCertificateDataStore {

    fun getUserTrustedCertificates(): List<CommonCertificate>

    suspend fun trustCertificates(certificates: List<CommonCertificate>)

    suspend fun deleteCertificate(sha256Hash: ByteString)
}
