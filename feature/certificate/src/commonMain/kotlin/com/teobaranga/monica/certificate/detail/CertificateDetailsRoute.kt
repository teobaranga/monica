package com.teobaranga.monica.certificate.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
data class CertificateDetailsRoute(val sha256Hash: String)

internal fun NavGraphBuilder.certificateDetailsScreen() {
    composable<CertificateDetailsRoute> { backStackEntry ->
        val navigator = LocalNavigator.current
        val viewModel = injectedViewModel<CertificateDetailsViewModel, CertificateDetailsViewModel.Factory>(
            creationCallback = { factory ->
                factory(createSavedStateHandle())
            },
        )
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
