package com.teobaranga.monica.certificate.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.core.network.CertificateTruster
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class CertificateIssueViewModel(
    private val certificateRepository: CertificateRepository,
    private val certificateTruster: CertificateTruster,
): ViewModel() {

    val hasUntrustedCertificates = certificateRepository.unsecureCertificates
        .map {
            it.isNotEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false,
        )

    fun onDismiss() {
        certificateRepository.clearUnsecureCertificates()
    }

    fun onTrust() {
        viewModelScope.launch {
            val certs = certificateRepository.unsecureCertificates.value.map { it.x509Certificate }
            certificateTruster.trustCertificates(certs)
            onDismiss()
        }
    }
}
