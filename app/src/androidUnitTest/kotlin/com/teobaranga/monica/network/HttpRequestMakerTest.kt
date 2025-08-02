package com.teobaranga.monica.network

import at.asitplus.signum.indispensable.toJcaCertificate
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.certificate.testCertificate
import com.teobaranga.monica.core.network.HttpRequestMaker
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import okio.ByteString.Companion.toByteString
import java.security.cert.CertPath
import java.security.cert.CertPathValidatorException
import java.security.cert.X509Certificate

class HttpRequestMakerTest : BehaviorSpec({

    lateinit var mockEngine: MockEngine.Queue
    lateinit var httpRequestMaker: HttpRequestMaker
    lateinit var certificateRepository: CertificateRepository

    fun setup() {
        val component = HttpRequestMakerComponent::class.create()
        mockEngine = component.httpEngine()
        httpRequestMaker = component.httpRequestMaker()
        certificateRepository = component.certificateRepository()
    }

    Given("a non-certificate exception") {
        setup()
        val exception = RuntimeException()
        mockEngine.enqueue {
            throw exception
        }

        When("making a call") {
            val result = httpRequestMaker.call {
                getApiResponse<Unit>("api")
            }

            Then("it should return the same exception") {
                (result as ApiResponse.Failure.Exception).throwable shouldBe exception
            }

            Then("it should not interact with the certificate repository") {
                certificateRepository.untrustedCertificates.value shouldBe emptyList()
            }
        }
    }

    Given("a certificate exception with no certificates") {
        setup()
        val certPath = mockk<CertPath> {
            every { certificates } returns emptyList()
        }
        val exception = CertPathValidatorException("Untrusted certificate", null, certPath, -1)
        mockEngine.enqueue {
            throw exception
        }

        When("making a call") {
            val result = httpRequestMaker.call {
                getApiResponse<Unit>("api")
            }

            Then("it should return the same exception") {
                (result as ApiResponse.Failure.Exception).throwable shouldBe exception
            }

            Then("it should not interact with the certificate repository") {
                certificateRepository.untrustedCertificates.value shouldBe emptyList()
            }
        }
    }

    Given("a certificate exception with certificates") {
        setup()
        val dispatcher = StandardTestDispatcher()
        val x509Certificate = mockk<X509Certificate>(relaxed = true) {
            every { encoded } returns testCertificate.encodeToDer()
        }
        val certPath = mockk<CertPath> {
            every { certificates } returns listOf(x509Certificate)
        }
        val exception = CertPathValidatorException("Untrusted certificate", null, certPath, -1)
        mockEngine.enqueue {
            throw exception
        }

        When("making a call") {
            val result = CoroutineScope(dispatcher).async {
                httpRequestMaker.call {
                    getApiResponse<Unit>("api")
                }
            }
            dispatcher.scheduler.runCurrent()

            And("the user accepts the certificate") {
                mockEngine.config.requestHandlers.clear()
                mockEngine.enqueue {
                    respond("success")
                }
                certificateRepository.acceptUntrustedCertificates()

                Then("it should retry the request and return the new response") {
                    dispatcher.scheduler.advanceUntilIdle()
                    result.await() shouldBe instanceOf<ApiResponse.Success<Unit>>()
                }

                Then("it should set the untrusted certificates") {
                    val certificates = async {
                        certificateRepository.userTrustedCertificates.first()
                    }
                    dispatcher.scheduler.advanceUntilIdle()

                    val testCertificateEncodedByteString = testCertificate.toJcaCertificate().getOrThrow()
                        .encoded.toByteString()
                    certificates.await().single() shouldBe CommonCertificate(
                        x509Certificate = testCertificate,
                        sha1 = testCertificateEncodedByteString.sha1(),
                        sha256 = testCertificateEncodedByteString.sha256(),
                    )
                }
            }

            And("the user rejects the certificate") {

                certificateRepository.refuseUntrustedCertificates()

                Then("it should return the same exception") {
                    dispatcher.scheduler.advanceUntilIdle()
                    (result.await() as ApiResponse.Failure.Exception).throwable shouldBe exception
                }

                Then("it should not interact with the certificate repository") {
                    dispatcher.scheduler.advanceUntilIdle()
                    val certificates = async {
                        certificateRepository.userTrustedCertificates.first()
                    }

                    certificateRepository.untrustedCertificates.value shouldBe emptyList()
                    certificates.await() shouldBe emptyList()
                }
            }
        }
    }
})
