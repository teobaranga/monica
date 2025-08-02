package com.teobaranga.monica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.util.Consumer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.LocalViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.applinks.AppLinksHandler
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.certificate.data.CertificateTrustStatus
import com.teobaranga.monica.certificate.detail.CertificateDetailsRoute
import com.teobaranga.monica.certificate.list.CertificateListRoute
import com.teobaranga.monica.certificate.popup.CertificateIssueBottomSheet
import com.teobaranga.monica.certificate.popup.CertificateIssueViewModel
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.home.HomeRoute
import kotlinx.coroutines.flow.map
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

class MainActivity : ComponentActivity() {

    private val intentListener = Consumer<Intent> { intent ->
        intent.data?.let {
            AppLinksHandler.handle(it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        addOnNewIntentListener(intentListener)

        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalViewModelFactoryOwner provides getViewModelFactoryOwner(),
                    LocalNavigator provides navController,
                    LocalWebBrowser provides AndroidWebBrowser(this),
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                        startDestination = HomeRoute,
                        builder = RootNavGraphBuilder,
                    )

                    UntrustedCertificatePopup(
                        navHostController = navController,
                    )
                }
            }
        }
    }

    @Composable
    private fun UntrustedCertificatePopup(navHostController: NavHostController) {
        val viewModel = injectedViewModel<CertificateIssueViewModel>()
        val hasUntrustedCertificates by viewModel.hasUntrustedCertificates.collectAsStateWithLifecycle()
        val isViewingDetails by navHostController.currentBackStackEntryFlow
            .map {
                it.destination.hasRoute(CertificateListRoute::class)
                    || it.destination.hasRoute(CertificateDetailsRoute::class)
            }
            .collectAsStateWithLifecycle(false)
        val shouldShow by remember {
            derivedStateOf {
                hasUntrustedCertificates && !isViewingDetails
            }
        }
        if (shouldShow) {
            CertificateIssueBottomSheet(
                onDismissRequest = viewModel::onDismiss,
                onViewDetails = {
                    navHostController.navigate(CertificateListRoute(CertificateTrustStatus.UNTRUSTED))
                },
                onReject = viewModel::onDismiss,
                onAccept = viewModel::onTrust,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOnNewIntentListener(intentListener)
    }

    private fun getViewModelFactoryOwner(): ViewModelFactoryOwner {
        return (applicationContext as ScopedViewModelFactoryProvider).getViewModelFactoryOwner(AppScope::class)
    }
}
