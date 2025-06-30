package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.core.inject.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.io.bytestring.hexToByteString
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalStdlibApi::class)
@Inject
@ContributesViewModel(scope = AppScope::class, assistedFactory = CertificateDetailsViewModel.Factory::class)
class CertificateDetailsViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    @param:ApplicationContext
    private val appScope: CoroutineScope,
    private val certificateRepository: CertificateRepository,
    private val certificateDetailsUiStateMapper: CertificateDetailsUiStateMapper,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<CertificateDetailsRoute>()

    val certificateDetails = certificateRepository.allCertificates
        .map { certificates ->
            certificates.find { it.sha256 == route.sha256Hash.hexToByteString() }?.let {
                certificateDetailsUiStateMapper.map(it)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = null,
        )

    fun onDelete() {
        appScope.launch {
            certificateRepository.deleteUserTrustedCertificate(route.sha256Hash.hexToByteString())
        }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): CertificateDetailsViewModel
    }
}
