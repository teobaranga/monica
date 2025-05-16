package com.teobaranga.monica.network

import android.content.Context
import com.diamondedge.logging.logging
import com.teobaranga.monica.core.inject.ApplicationContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertPathValidatorException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface SslSettings {
    fun getSslContext(): SSLContext
    fun getTrustManager(): X509TrustManager
    fun trustCertificates(ex: CertPathValidatorException)
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class SslSettingsImpl(@ApplicationContext private val context: Context): SslSettings {

    private fun createCombinedKeyStore(): KeyStore {
        // Load system KeyStore
        val systemKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(null as KeyStore?)

        // Convert TrustManager to KeyStore
        var systemTrustManager: X509TrustManager? = null
        for (tm in tmf.trustManagers) {
            if (tm is X509TrustManager) {
                systemTrustManager = tm
                break
            }
        }

        checkNotNull(systemTrustManager) { "System X509TrustManager not found" }

        // Create a new KeyStore with all certificates
        val combinedKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        combinedKeyStore.load(null, null)

        // Add system certificates
        var systemCertCount = 0
        for (cert in systemTrustManager.acceptedIssuers) {
            val alias = "system_" + systemCertCount++
            combinedKeyStore.setCertificateEntry(alias, cert)
        }


        // Add our custom certificates
        val keyStore = getKeyStore()
        val aliases = keyStore.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            val cert = keyStore.getCertificate(alias)
            combinedKeyStore.setCertificateEntry("custom_" + alias, cert)
            logging.d { "Adding custom cert: $alias" }
        }

        logging.d {
            "Combined KeyStore created with " + systemCertCount +
                " system certs and " + keyStore.size() + " custom certs"
        }

        return combinedKeyStore
    }

    private fun getKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val keyStorePassword = "foobar".toCharArray()
        val file = File(context.filesDir, "keystore.jks")
        if (file.exists()) {
            val keyStoreFile = FileInputStream(file)
            keyStore.load(keyStoreFile, keyStorePassword)
            logging.i { "KeyStore found" }
        } else {
            keyStore.load(null, keyStorePassword)
            logging.i { "KeyStore not found, creating new one" }
        }
        return keyStore
    }

    private fun getTrustManagerFactory(): TrustManagerFactory {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(createCombinedKeyStore())
        return trustManagerFactory
    }

    override fun getSslContext(): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(getTrustManager()), null)
        return sslContext
    }

    override fun getTrustManager(): X509TrustManager {
        return getTrustManagerFactory().trustManagers.first { it is X509TrustManager } as X509TrustManager
    }

    override fun trustCertificates(ex: CertPathValidatorException) {
        val customKeyStore = getKeyStore()
        ex.certPath.certificates.forEach {
            val alias = "custom_${it.hashCode()}"
            customKeyStore.setCertificateEntry(alias, it)
            logging.d { "Added certificate to combined KeyStore: $alias" }
        }
        val file = File(context.filesDir, "keystore.jks")
        customKeyStore.store(file.outputStream().buffered(), "foobar".toCharArray())
    }

    companion object {
        val logging = logging()
    }
}
