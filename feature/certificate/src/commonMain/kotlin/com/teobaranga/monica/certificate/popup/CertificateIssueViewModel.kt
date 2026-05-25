package com.teobaranga.monica.certificate.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.certificate.data.CertificateRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Inject
@ContributesIntoMap(AppScope::class)
@ViewModelKey
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
