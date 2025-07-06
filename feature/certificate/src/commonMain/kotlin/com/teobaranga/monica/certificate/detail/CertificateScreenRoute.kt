package com.teobaranga.monica.certificate.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import kotlinx.serialization.Serializable

@Serializable
data class CertificateScreenRoute(val sha256Hash: String)

fun NavGraphBuilder.certificateScreen() {
    composable<CertificateScreenRoute> { backStackEntry ->
        val viewModel = injectedViewModel<CertificateViewModel, CertificateViewModel.Factory>(
            creationCallback = { factory ->
                factory(createSavedStateHandle())
            },
        )
        val certificateDetails by viewModel.certificateDetails.collectAsStateWithLifecycle()
        certificateDetails?.let {
            CertificateScreen(it)
        }
    }
}
