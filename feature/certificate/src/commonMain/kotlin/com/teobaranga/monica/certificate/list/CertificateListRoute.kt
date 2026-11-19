package com.teobaranga.monica.certificate.list

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.core.navigation.navKeySerializer
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable

@Serializable
data class CertificateListRoute(private val value: String) : NavKey {

    // Workaround due to https://youtrack.jetbrains.com/issue/CMP-8270/

    constructor(certificateTrustStatus: CertificateTrustStatus) : this(certificateTrustStatus.name)

    val certificateTrustStatus
        get() = CertificateTrustStatus.valueOf(value)
}

@Inject
@ContributesIntoSet(AppScope::class)
class CertificateListRouteSerializer : NavKeySerializer by navKeySerializer(CertificateListRoute.serializer())

internal fun EntryProviderScope<NavKey>.certificateListScreen() {
    entry<CertificateListRoute> { route ->
        val navigator = LocalNavigator.current
        val viewModel = assistedMetroViewModel<CertificateListViewModel, CertificateListViewModel.Factory>(
            createViewModel = {
                create(route.certificateTrustStatus)
            },
        )
        val userCertificates by viewModel.certificatesListItem.collectAsStateWithLifecycle()
        CertificateListScreen(
            certificateTrustStatus = route.certificateTrustStatus,
            certificateListItems = userCertificates,
            onNavigateToCertificate = {
                navigator.navigate(CertificateDetailsRoute(it))
            },
            onBack = navigator::popBackStack,
        )
    }
}
