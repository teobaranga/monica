package com.teobaranga.monica.certificate

import androidx.lifecycle.ViewModel
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(scope = AppScope::class)
class CertificateViewModel(
    private val certificateRepository: CertificateRepository,
) : ViewModel() {

    val certificates
        get() = certificateRepository.certificates
}
