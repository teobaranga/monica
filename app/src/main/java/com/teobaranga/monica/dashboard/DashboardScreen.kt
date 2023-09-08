package com.teobaranga.monica.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.destinations.DashboardDestination
import com.teobaranga.monica.destinations.SetupDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun Dashboard(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<DashboardViewModel>()

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == false) {
            navigator.navigate(SetupDestination) {
                popUpTo(DashboardDestination.route) {
                    inclusive = true
                }
            }
        }
    }

    if (isLoggedIn == true) {
        DashboardScreen(
            onClearAuthorization = viewModel::onClearAuthorization,
        )
    } else {
        // TODO Replace with Loading screen
        Box(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun DashboardScreen(
    onClearAuthorization: () -> Unit,
) {
    MonicaBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .imePadding(),
        ) {
            Text(text = "Dashboard")

            Button(
                onClick = onClearAuthorization,
            ) {
                Text(text = "Sign out")
            }
        }
    }
}
