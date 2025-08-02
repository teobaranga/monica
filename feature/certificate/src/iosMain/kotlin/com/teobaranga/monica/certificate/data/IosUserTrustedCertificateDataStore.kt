package com.teobaranga.monica.certificate.data

import me.tatarka.inject.annotations.Inject
import okio.ByteString
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

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
