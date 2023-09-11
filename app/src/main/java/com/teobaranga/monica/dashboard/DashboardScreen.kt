package com.teobaranga.monica.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.destinations.DashboardDestination
import com.teobaranga.monica.destinations.SetupDestination
import com.teobaranga.monica.home.HomeSearchBar
import com.teobaranga.monica.home.HomeTab
import com.teobaranga.monica.ui.theme.MonicaTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onClearAuthorization: () -> Unit,
) {
    var currentTab by rememberSaveable { mutableStateOf(HomeTab.DASHBOARD) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier,
            ) {
                for (homeTab in HomeTab.entries) {
                    NavigationBarItem(
                        selected = currentTab == homeTab,
                        onClick = {
                            currentTab = homeTab
                        },
                        label = {
                            Text(text = homeTab.label)
                        },
                        icon = {
                            Icon(
                                painter = rememberVectorPainter(image = homeTab.icon),
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        },
    ) { contentPadding ->
        HomeSearchBar(
            modifier = Modifier
                .padding(contentPadding),
        )
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(top = SearchBarDefaults.InputFieldHeight),
        ) {
            Button(
                onClick = onClearAuthorization,
            ) {
                Text(text = "Sign out")
            }
        }
    }
}

@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
)
@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewDashboardScreen() {
    MonicaTheme {
        MonicaBackground {
            DashboardScreen(
                onClearAuthorization = { },
            )
        }
    }
}
