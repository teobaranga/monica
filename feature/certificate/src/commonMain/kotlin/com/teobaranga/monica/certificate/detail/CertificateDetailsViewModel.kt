package com.teobaranga.monica.certificate.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.certificate.data.CommonCertificate
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
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
    sha256HashHex: String,
    @param:ApplicationContext
    private val appScope: CoroutineScope,
    private val certificateRepository: CertificateRepository,
    private val certificateDetailsUiStateMapper: CertificateDetailsUiStateMapper,
) : ViewModel() {

    private val sha256Hash = sha256HashHex.decodeHex()

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
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory: ManualViewModelAssistedFactory {
        fun create(sha256HashHex: String): CertificateDetailsViewModel
    }
}
