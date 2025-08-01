package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.toJcaCertificate
import at.asitplus.signum.indispensable.toKmpCertificate
import com.teobaranga.monica.core.network.SslSettings
import me.tatarka.inject.annotations.Inject
import okio.ByteString
import okio.ByteString.Companion.toByteString
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.cert.X509Certificate

@Inject
@ContributesBinding(AppScope::class)
class AndroidUserTrustedCertificateDataStore(
    private val sslSettings: SslSettings,
) : UserTrustedCertificateDataStore {

    override fun getUserTrustedCertificates(): List<CommonCertificate> {
        return sslSettings.getUserTrustedCertificates()
            .mapNotNull {
                val x509Cert = (it as? X509Certificate) ?: return@mapNotNull null
                val x509EncodedByteString = x509Cert.encoded.toByteString()
                CommonCertificate(
                    x509Certificate = x509Cert.toKmpCertificate().getOrNull() ?: return@mapNotNull null,
                    sha1 = x509EncodedByteString.sha1(),
                    sha256 = x509EncodedByteString.sha256(),
                )
            }
            .toList()
    }

    override suspend fun trustCertificates(certificates: List<CommonCertificate>) {
        val jcaCertificates = certificates.mapNotNull {
            it.x509Certificate.toJcaCertificate().getOrNull()
        }
        sslSettings.trustCertificates(jcaCertificates)
    }

    override suspend fun deleteCertificate(sha256Hash: ByteString) {
        sslSettings.deleteCertificate(sha256Hash)
    }
}
