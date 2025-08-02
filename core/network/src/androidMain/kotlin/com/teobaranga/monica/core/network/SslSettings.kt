package com.teobaranga.monica.core.network

import android.content.Context
import com.diamondedge.logging.logging
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.inject.ApplicationContext
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.ByteString
import okio.ByteString.Companion.toByteString
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface SslSettings {

    fun getSslContext(): SSLContext

    fun getTrustManager(): X509TrustManager

    fun getUserTrustedCertificates(): Sequence<Certificate>

    fun trustCertificates(certificates: List<Certificate>)

    suspend fun deleteCertificate(sha256Hash: ByteString)
}

private const val KEYSTORE_FILE_NAME = "keystore.jks"
private const val KEYSTORE_PASSWORD = "foobar"

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class SslSettingsImpl(
    @param:ApplicationContext
    private val context: Context,
    private val dispatcher: Dispatcher,
) : SslSettings {

    private fun createCombinedKeyStore(): KeyStore {
        // Load system KeyStore
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
        keyStore.aliases().asSequence()
            .forEach { alias ->
                val cert = keyStore.getCertificate(alias)
                combinedKeyStore.setCertificateEntry("custom_$alias", cert)
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
        val keyStorePassword = KEYSTORE_PASSWORD.toCharArray()
        val file = File(context.filesDir, KEYSTORE_FILE_NAME)
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

    override fun getUserTrustedCertificates(): Sequence<Certificate> {
        val keyStore = getKeyStore()
        return keyStore.aliases().asSequence()
            .map { alias ->
                keyStore.getCertificate(alias)
            }
    }

    override fun getTrustManager(): X509TrustManager {
        return getTrustManagerFactory().trustManagers.first { it is X509TrustManager } as X509TrustManager
    }

    override fun trustCertificates(certificates: List<Certificate>) {
        val keyStore = getKeyStore()
        certificates
            .forEach {
                val alias = "custom_${it.hashCode()}"
                keyStore.setCertificateEntry(alias, it)
                logging.d { "Added certificate to combined KeyStore: $alias" }
            }
        keyStore.storeToFiles()
    }

    override suspend fun deleteCertificate(sha256Hash: ByteString) {
        withContext(dispatcher.io) {
            val keyStore = getKeyStore()
            val certAlias = keyStore.aliases().asSequence()
                .firstOrNull { alias ->
                    val x509Certificate = keyStore.getCertificate(alias) as? X509Certificate
                        ?: return@firstOrNull false
                    sha256Hash == x509Certificate.encoded.toByteString().sha256()
                }
            if (certAlias != null) {
                logging.i { "Deleting certificate: $certAlias" }
                keyStore.deleteEntry(certAlias)
                keyStore.storeToFiles()
                logging.i { "Deleted certificate: $certAlias" }
            }
        }
    }

    private fun KeyStore.storeToFiles() {
        val file = File(context.filesDir, KEYSTORE_FILE_NAME)
        store(file.outputStream().buffered(), KEYSTORE_PASSWORD.toCharArray())
        isStale = true
    }

    companion object {
        val logging = logging()
    }
}
