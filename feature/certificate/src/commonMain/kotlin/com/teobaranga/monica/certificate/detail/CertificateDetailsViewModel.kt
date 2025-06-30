package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.core.inject.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.io.bytestring.ByteString
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

    private val sha256Hash = savedStateHandle.toRoute<CertificateDetailsRoute>().sha256Hash.hexToByteString()

    val certificateDetails = combine(
        certificateRepository.untrustedCertificates,
        certificateRepository.userTrustedCertificates,
    ) { untrustedCertificates, userTrustedCertificates ->
        untrustedCertificates.findBySha256Hash(sha256Hash)?.let { certificate ->
            certificateDetailsUiStateMapper.map(certificate, false)
        } ?: userTrustedCertificates.findBySha256Hash(sha256Hash)?.let { certificate ->
            certificateDetailsUiStateMapper.map(certificate, true)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = null,
    )

    fun onDelete() {
        appScope.launch {
            certificateRepository.deleteUserTrustedCertificate(sha256Hash)
        }
    }

    private fun List<CommonCertificate>.findBySha256Hash(sha256Hash: ByteString): CommonCertificate? {
        return find { it.sha256 == sha256Hash }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): CertificateDetailsViewModel
    }
}
