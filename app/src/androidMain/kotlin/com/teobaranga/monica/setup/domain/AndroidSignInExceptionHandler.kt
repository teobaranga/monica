package com.teobaranga.monica.setup.domain

import at.asitplus.signum.indispensable.toKmpCertificate
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.MessageDigest
import java.security.cert.CertPathValidatorException
import java.security.cert.X509Certificate

private val hexFormat = HexFormat {
    upperCase = true
    bytes {
        byteSeparator = ":"
    }
}

@Inject
@ContributesBinding(AppScope::class)
class AndroidSignInExceptionHandler(
    private val certificateRepository: CertificateRepository,
) : SignInExceptionHandler {

    override fun handle(exception: Throwable): SignInResult.Error {
        if (exception is CertPathValidatorException) {
            exception.certPath?.let { certPath ->
                val certificates = certPath.certificates
                    .mapNotNull { certificate ->
                        if (certificate !is X509Certificate) return@mapNotNull null

                        println(certificate)
                        val x509Certificate = certificate.toKmpCertificate().getOrNull()
                            ?: return@mapNotNull null
                        CommonCertificate(
                            x509Certificate = x509Certificate,
                            sha1 = certificate.getFingerprint("SHA-1"),
                            sha256 = certificate.getFingerprint("SHA-256"),
                        )
                    }
                certificateRepository.setUnsecureCertificates(certificates)
                return SignInResult.Error.UntrustedCertificates
            }
        }
        return SignInResult.Error.UnknownError
    }

    private fun X509Certificate.getFingerprint(algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(encoded)
        return bytes.toHexString(hexFormat)
    }
}
