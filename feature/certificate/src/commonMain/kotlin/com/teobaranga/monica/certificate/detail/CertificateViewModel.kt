package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(scope = AppScope::class)
class CertificateViewModel(
    certificateRepository: CertificateRepository,
    private val certificateDetailsUiStateMapper: CertificateDetailsUiStateMapper,
) : ViewModel() {

    val certificateDetails = certificateRepository.unsecureCertificates
        .map {
            it.firstOrNull()?.let { certificate ->
                certificateDetailsUiStateMapper.map(certificate)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = null,
        )
}
