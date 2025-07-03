package com.teobaranga.monica.certificate

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object CertificateScreenRoute

fun NavGraphBuilder.certificateScreen() {
    composable<CertificateScreenRoute> {

        val viewModel = injectedViewModel<CertificateViewModel>()
        viewModel.certificateDetails?.let {
            CertificateScreen(it)
        }
    }
}
