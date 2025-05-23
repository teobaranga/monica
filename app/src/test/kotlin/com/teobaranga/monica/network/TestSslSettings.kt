package com.teobaranga.monica.network

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.KeyStore
import java.security.cert.CertPathValidatorException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Inject
@ContributesBinding(
    scope = AppScope::class,
    replaces = [SslSettingsImpl::class]
)
class TestSslSettings: SslSettings {

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

    override fun trustCertificates(ex: CertPathValidatorException) {
        // no-op
    }
}
