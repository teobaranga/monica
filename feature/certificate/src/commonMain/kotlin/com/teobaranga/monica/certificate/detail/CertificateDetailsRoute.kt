package com.teobaranga.monica.certificate.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable

@Serializable
data class CertificateDetailsRoute(val sha256Hash: String)

internal fun NavGraphBuilder.certificateDetailsScreen() {
    composable<CertificateDetailsRoute> {
        val navigator = LocalNavigator.current
        val viewModel = assistedMetroViewModel<CertificateDetailsViewModel>()
        val certificateDetails by viewModel.certificateDetails.collectAsStateWithLifecycle()
        certificateDetails?.let {
            CertificateDetailsScreen(
                certificateData = it,
                onBack = navigator::popBackStack,
                onDelete = {
                    viewModel.onDelete()
                    navigator.popBackStack()
                },
            )
        }
    }
}
