package com.teobaranga.monica

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.teobaranga.monica.certificate.popup.UntrustedCertificatePopup
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.home.HomeRoute

@Composable
fun MonicaApp(
    navController: NavHostController,
) {
    MonicaTheme(
        dynamicColor = false,
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
