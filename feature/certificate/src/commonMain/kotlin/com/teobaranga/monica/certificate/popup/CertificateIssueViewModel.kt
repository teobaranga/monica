package com.teobaranga.monica.certificate.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
): ViewModel() {

    val hasUntrustedCertificates = certificateRepository.untrustedCertificates
        .map {
            it.isNotEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false,
        )

    fun onDismiss() {
        certificateRepository.refuseUntrustedCertificates()
    }

    fun onTrust() {
        viewModelScope.launch {
            certificateRepository.acceptUntrustedCertificates()
        }
    }
}
