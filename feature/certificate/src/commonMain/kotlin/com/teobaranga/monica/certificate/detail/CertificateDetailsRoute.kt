package com.teobaranga.monica.certificate.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable

@Serializable
data class CertificateDetailsRoute(val sha256Hash: String) : NavKey

@Inject
@ContributesIntoSet(AppScope::class)
class CertificateDetailsRouteSerializer : NavKeySerializer by navKeySerializer(CertificateDetailsRoute.serializer())

internal fun EntryProviderScope<NavKey>.certificateDetailsScreen() {
    entry<CertificateDetailsRoute> { route ->
        val navigator = LocalNavigator.current
        val viewModel = assistedMetroViewModel<CertificateDetailsViewModel, CertificateDetailsViewModel.Factory>(
            createViewModel = {
                create(route.sha256Hash)
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
