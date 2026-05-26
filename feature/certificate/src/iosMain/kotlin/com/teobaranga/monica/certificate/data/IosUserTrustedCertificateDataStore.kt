package com.teobaranga.monica.certificate.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import okio.ByteString

// TODO
@Inject
@ContributesBinding(AppScope::class)
class IosUserTrustedCertificateDataStore : UserTrustedCertificateDataStore {

    override fun getUserTrustedCertificates(): List<CommonCertificate> {
        return emptyList()
    }

    override suspend fun trustCertificates(certificates: List<CommonCertificate>) = Unit

    override suspend fun deleteCertificate(sha256Hash: ByteString) = Unit
}
