package com.teobaranga.monica.certificate.list

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.certificate.detail.CertificateScreenRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
data object CertificateListRoute

fun NavGraphBuilder.certificateListScreen() {
    composable<CertificateListRoute> {
        val navigator = LocalNavigator.current
        val viewModel = injectedViewModel<CertificateListViewModel>()
        val userCertificates by viewModel.userCertificates.collectAsStateWithLifecycle()
        CertificateListScreen(
            userCertificates = userCertificates,
            onNavigateToCertificate = {
                navigator.navigate(CertificateScreenRoute(it))
            },
            onBack = navigator::popBackStack,
        )
    }
}
