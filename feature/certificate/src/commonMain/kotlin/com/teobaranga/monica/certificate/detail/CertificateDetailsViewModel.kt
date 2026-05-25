package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalStdlibApi::class)
@AssistedInject
class CertificateDetailsViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    @param:ApplicationContext
    private val appScope: CoroutineScope,
    private val certificateRepository: CertificateRepository,
    private val certificateDetailsUiStateMapper: CertificateDetailsUiStateMapper,
) : ViewModel() {

    private val sha256Hash = savedStateHandle.toRoute<CertificateDetailsRoute>().sha256Hash.decodeHex()

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
    @ViewModelAssistedFactoryKey(CertificateDetailsViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory: ViewModelAssistedFactory {

        override fun create(extras: CreationExtras): ViewModel {
            return create(extras.createSavedStateHandle())
        }

        fun create(@Assisted savedStateHandle: SavedStateHandle): CertificateDetailsViewModel
    }
}
