package com.teobaranga.monica.network

import com.teobaranga.monica.core.network.SslSettings
import com.teobaranga.monica.core.network.SslSettingsImpl
import com.teobaranga.monica.core.network.util.sha256
import com.teobaranga.monica.core.network.util.toByteString
import kotlinx.io.bytestring.ByteString
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.KeyStore
import java.security.cert.Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Inject
@ContributesBinding(
    scope = AppScope::class,
    replaces = [SslSettingsImpl::class]
)
class TestSslSettings : SslSettings {

    private val userTrustedCertificates = mutableListOf<Certificate>()

    override fun getSslContext(): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(getTrustManager()), null)
        return sslContext
    }

    override fun getTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        return trustManagerFactory.trustManagers.first { it is X509TrustManager } as X509TrustManager
    }

    override fun getUserTrustedCertificates(): Sequence<Certificate> {
        return userTrustedCertificates.asSequence()
    }

    override fun trustCertificates(certificates: List<Certificate>) {
        userTrustedCertificates.addAll(certificates)
    }

    override suspend fun deleteCertificate(sha256Hash: ByteString) {
        userTrustedCertificates.removeIf { it.encoded.toByteString().sha256() == sha256Hash }
    }
}
