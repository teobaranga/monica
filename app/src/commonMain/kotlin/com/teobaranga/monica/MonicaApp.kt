package com.teobaranga.monica

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.compose.setSingletonImageLoaderFactory
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.certificate.popup.UntrustedCertificatePopup
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.dashboard.DashboardRoute
import com.teobaranga.monica.home.HomeScreen
import com.teobaranga.monica.home.HomeViewModel
import com.teobaranga.monica.home.topLevelRoutes
import com.teobaranga.monica.navigation.MonicaNavigator
import com.teobaranga.monica.navigation.rememberMonicaNavigationState
import com.teobaranga.monica.setup.Setup
import com.teobaranga.monica.ui.navigation.TopLevelRoute
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun MonicaApp(
    viewModel: HomeViewModel = metroViewModel(),
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    setSingletonImageLoaderFactory {
        viewModel.imageLoader
    }

    MonicaTheme(
        dynamicColor = false,
    ) {
        when (isLoggedIn) {
            true -> LoggedInApp(
                savedStateConfiguration = viewModel.savedStateConfiguration,
                entryProvider = viewModel.entryProvider,
            )
            false -> Setup()
            else -> Unit
        }
    }
}

@Composable
private fun LoggedInApp(
    savedStateConfiguration: SavedStateConfiguration,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
) {
    val navigationState = rememberMonicaNavigationState(
        configuration = savedStateConfiguration,
        startRoute = DashboardRoute,
        topLevelRoutes = topLevelRoutes.map(TopLevelRoute::route),
    )
    val navigator = remember(navigationState) {
        MonicaNavigator(navigationState)
    }
    val currentRoute = navigationState.currentBackStack.last()

    CompositionLocalProvider(
        LocalNavigator provides navigator,
    ) {
        HomeScreen(
            navigationState = navigationState,
            entryProvider = entryProvider,
        )

        UntrustedCertificatePopup(
            isViewingCertificates = currentRoute is CertificateListRoute ||
                currentRoute is CertificateDetailsRoute,
            onViewDetails = {
                navigator.navigate(CertificateListRoute(CertificateTrustStatus.UNTRUSTED))
            },
        )
    }
}
