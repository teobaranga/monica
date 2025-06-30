package com.teobaranga.monica.certificate.list

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
data class CertificateListRoute(val type: Type) {
    enum class Type {
        UNTRUSTED,
        TRUSTED,
    }
}

internal fun NavGraphBuilder.certificateListScreen() {
    composable<CertificateListRoute> {
        val navigator = LocalNavigator.current
        val viewModel = injectedViewModel<CertificateListViewModel, CertificateListViewModel.Factory>(
            creationCallback = { factory ->
                factory(createSavedStateHandle())
            },
        )
        val userCertificates by viewModel.certificatesListItem.collectAsStateWithLifecycle()
        CertificateListScreen(
            type = viewModel.route.type,
            certificateListItems = userCertificates,
            onNavigateToCertificate = {
                navigator.navigate(CertificateDetailsRoute(it))
            },
            onBack = navigator::popBackStack,
        )
    }
}
