package com.teobaranga.monica.certificate.data

import at.asitplus.signum.indispensable.toKmpCertificate
import com.teobaranga.monica.core.network.SslSettings
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.MessageDigest
import java.security.cert.X509Certificate

private val hexFormat = HexFormat {
    upperCase = true
    bytes {
        byteSeparator = ":"
    }
}

@Inject
@ContributesBinding(AppScope::class)
class AndroidUserTrustedCertificateDataStore(
    private val sslSettings: SslSettings,
): UserTrustedCertificateDataStore {

    override fun getUserTrustedCertificates(): List<CommonCertificate> {
        return sslSettings.getUserTrustedCertificates()
            .mapNotNull {
                val x509Cert = (it as? X509Certificate) ?: return@mapNotNull null
                CommonCertificate(
                    x509Certificate = x509Cert.toKmpCertificate().getOrNull() ?: return@mapNotNull null,
                    sha1 = x509Cert.getFingerprint("SHA-1"),
                    sha256 = x509Cert.getFingerprint("SHA-256"),
                )
            }
            .toList()
    }

    // TODO duplicate
    private fun X509Certificate.getFingerprint(algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(encoded)
        return bytes.toHexString(hexFormat)
    }
}
