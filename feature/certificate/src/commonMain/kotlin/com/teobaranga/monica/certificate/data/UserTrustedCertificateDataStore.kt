package com.teobaranga.monica.certificate.data

interface UserTrustedCertificateDataStore {

    fun getUserTrustedCertificates(): List<CommonCertificate>
}
