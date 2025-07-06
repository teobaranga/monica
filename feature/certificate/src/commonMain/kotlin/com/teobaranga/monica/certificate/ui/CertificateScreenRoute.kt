package com.teobaranga.monica.certificate.ui

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object CertificateScreenRoute

fun NavGraphBuilder.certificateScreen() {
    composable<CertificateScreenRoute> {

        val viewModel = injectedViewModel<CertificateViewModel>()
        val certificateDetails by viewModel.certificateDetails.collectAsStateWithLifecycle()
        certificateDetails?.let {
            CertificateScreen(it)
        }
    }
}
