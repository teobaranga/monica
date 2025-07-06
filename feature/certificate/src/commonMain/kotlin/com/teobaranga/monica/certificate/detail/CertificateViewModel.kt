package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(
    scope = AppScope::class,
    assistedFactory = CertificateViewModel.Factory::class,
)
class CertificateViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    certificateRepository: CertificateRepository,
    private val certificateDetailsUiStateMapper: CertificateDetailsUiStateMapper,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<CertificateScreenRoute>()

    val certificateDetails = certificateRepository.unsecureCertificates
        .map {
            it
                .find { it.sha256 == route.sha256Hash }?.let {
                    certificateDetailsUiStateMapper.map(it)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = null,
        )
    
    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): CertificateViewModel
    }
}
