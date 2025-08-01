package com.teobaranga.monica.network

import at.asitplus.signum.indispensable.toKmpCertificate
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.certificate.data.UntrustedCertificateResult
import com.teobaranga.monica.core.network.ApiExceptionHandler
import io.ktor.client.HttpClient
import io.ktor.util.rootCause
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import me.tatarka.inject.annotations.Inject
import okio.ByteString.Companion.toByteString
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.cert.CertPathValidatorException
import java.security.cert.X509Certificate
import kotlin.time.Duration.Companion.minutes

@Inject
@ContributesBinding(scope = AppScope::class)
class AndroidApiExceptionHandler(
    private val httpClient: () -> HttpClient,
    private val certificateRepository: CertificateRepository,
) : ApiExceptionHandler {

    @OptIn(InternalAPI::class)
    override suspend fun <T> handle(
        response: ApiResponse.Failure.Exception,
        request: suspend HttpClient.() -> ApiResponse<T>,
    ): ApiResponse<T> {
        var handledResponse: ApiResponse<T> = response
        val rootCause = response.throwable.rootCause
        if (rootCause is CertPathValidatorException) {
            handleCertificateException(rootCause, request)?.let {
                handledResponse = it
            }
        }
        return handledResponse
    }

    /**
     * Extracts untrusted certificates and retries the request if the user accepted them.
     */
    private suspend fun <T> handleCertificateException(
        exception: CertPathValidatorException,
        request: suspend HttpClient.() -> ApiResponse<T>,
    ): ApiResponse<T>? {
        val certificates = (exception.certPath?.certificates ?: emptyList())
            .filterIsInstance<X509Certificate>()
            .mapNotNull { certificate ->
                certificate.toKmpCertificate()
                    .map { x509Certificate ->
                        val publicKeyByteString = certificate.encoded.toByteString()
                        CommonCertificate(
                            x509Certificate = x509Certificate,
                            sha1 = publicKeyByteString.sha1(),
                            sha256 = publicKeyByteString.sha256(),
                        )
                    }
                    .getOrNull()
            }
        if (certificates.isNotEmpty()) {
            certificateRepository.setUntrustedCertificates(certificates)
            // 5 minutes seems like a reasonable time for the user to review the certificates
            val result = withTimeoutOrNull(5.minutes) {
                certificateRepository.untrustedCertificateResult.first()
            }
            if (result == UntrustedCertificateResult.Accepted) {
                return httpClient().request()
            }
        }
        return null
    }
}
