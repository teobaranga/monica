package com.teobaranga.monica.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.NavGraphs
import com.teobaranga.monica.destinations.DashboardDestination
import com.teobaranga.monica.destinations.SetupDestination
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme

@RootNavGraph(start = true)
@Destination
@Composable
fun Home(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<HomeViewModel>()

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
        HomeScreen()
    } else {
        // TODO Replace with Loading screen
        Box(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun HomeScreen(
) {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()
    Scaffold(
        modifier = Modifier,
        bottomBar = {
            HomeNavigationBar(
                navController = navController,
            )
        },
    ) { contentPadding ->
        DestinationsNavHost(
            modifier = Modifier
                .padding(contentPadding),
            navGraph = NavGraphs.rootHome,
            engine = engine,
            navController = navController,
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewHomeScreen() {
    MonicaTheme {
        MonicaBackground {
            HomeScreen()
        }
    }
}
