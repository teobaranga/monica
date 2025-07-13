package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.toJcaCertificate
import at.asitplus.signum.indispensable.toKmpCertificate
import com.teobaranga.monica.certificate.util.sha1
import com.teobaranga.monica.certificate.util.sha256
import com.teobaranga.monica.certificate.util.toByteString
import com.teobaranga.monica.core.network.SslSettings
import kotlinx.io.bytestring.ByteString
import me.tatarka.inject.annotations.Inject
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
                val publicKeyByteString = x509Cert.publicKey.encoded.toByteString()
                CommonCertificate(
                    x509Certificate = x509Cert.toKmpCertificate().getOrNull() ?: return@mapNotNull null,
                    sha1 = publicKeyByteString.sha1(),
                    sha256 = publicKeyByteString.sha256(),
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
