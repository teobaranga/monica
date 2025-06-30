package com.teobaranga.monica.setup.domain

import com.teobaranga.monica.certificate.BasicConstraints
import com.teobaranga.monica.certificate.CertificateData
import com.teobaranga.monica.certificate.CertificateRepository
import com.teobaranga.monica.certificate.Fingerprints
import com.teobaranga.monica.certificate.IssuerName
import com.teobaranga.monica.certificate.KeyUsages
import com.teobaranga.monica.certificate.Miscellaneous
import com.teobaranga.monica.certificate.PublicKeyInfo
import com.teobaranga.monica.certificate.SubjectName
import com.teobaranga.monica.certificate.Validity
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.security.MessageDigest
import java.security.cert.CertPathValidatorException
import java.security.cert.X509Certificate
import kotlin.time.toKotlinInstant

@Inject
@ContributesBinding(AppScope::class)
class AndroidSignInExceptionHandler(
    private val certificateRepository: CertificateRepository,
) : SignInExceptionHandler {

    override fun handle(exception: Throwable): SignInResult.Error {
        if (exception is CertPathValidatorException) {
            val certificates = exception.certPath.certificates
                .mapNotNull {
                    if (it is X509Certificate) {
                        it.toCertificateData()
                    } else {
                        null
                    }
                }
            certificateRepository.certificates = certificates
            return SignInResult.Error.UntrustedCertificates(certificates)
        }
        return SignInResult.Error.UnknownError
    }

    private fun X509Certificate.toCertificateData(): CertificateData {
        return CertificateData(
            subjectName = SubjectName(
                commonName = subjectX500Principal.getField("CN"),
            ),
            issuerName = IssuerName(
                commonName = issuerX500Principal.getField("CN"),
            ),
            validity = Validity(
                notBefore = notBefore.toInstant().toKotlinInstant(),
                notAfter = notAfter.toInstant().toKotlinInstant(),
            ),
            publicKeyInfo = PublicKeyInfo(
                algorithm = publicKey.algorithm,
                keySize = publicKey.keySize,
                publicValue = publicKey.encoded.toHexString(),
            ),
            miscellaneous = Miscellaneous(
                serialNumber = serialNumber.toString(16),
                signatureAlgorithm = sigAlgName,
                version = version,
            ),
            fingerprints = Fingerprints(
                sha256 = getFingerprint("SHA-256"),
                sha1 = getFingerprint("SHA-1"),
            ),
            basicConstraints = BasicConstraints(
                certificateAuthority = basicConstraints != -1,
            ),
            keyUsages = KeyUsages(
                purposes = getKeyUsages(),
            ),
            subjectKeyId = getExtensionValue("2.5.29.14")?.toHexString() ?: "",
            authorityKeyId = getExtensionValue("2.5.29.35")?.toHexString() ?: "",
        )
    }

    private fun ByteArray.toHexString() = joinToString(separator = ":") {
        String.format("%02X", it)
    }

    private val java.security.PublicKey.keySize: Int
        get() {
            return when (this) {
                is java.security.interfaces.RSAPublicKey -> modulus.bitLength()
                is java.security.interfaces.ECPublicKey -> params.order.bitLength()
                else -> -1
            }
        }

    private fun X509Certificate.getKeyUsages(): List<String> {
        val keyUsage = keyUsage ?: return emptyList()
        val purposes = mutableListOf<String>()
        if (keyUsage[0]) purposes.add("Digital Signature")
        if (keyUsage[1]) purposes.add("Non-Repudiation")
        if (keyUsage[2]) purposes.add("Key Encipherment")
        if (keyUsage[3]) purposes.add("Data Encipherment")
        if (keyUsage[4]) purposes.add("Key Agreement")
        if (keyUsage[5]) purposes.add("Key Cert Sign")
        if (keyUsage[6]) purposes.add("CRL Sign")
        if (keyUsage[7]) purposes.add("Encipher Only")
        if (keyUsage[8]) purposes.add("Decipher Only")
        return purposes
    }

    private fun javax.security.auth.x500.X500Principal.getField(field: String): String {
        return name.split(",").find {
            it.startsWith("$field=")
        }?.substringAfter("=") ?: ""
    }

    private fun X509Certificate.getFingerprint(algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(encoded)
        return bytes.joinToString(separator = ":") {
            String.format("%02X", it)
        }
    }
}
