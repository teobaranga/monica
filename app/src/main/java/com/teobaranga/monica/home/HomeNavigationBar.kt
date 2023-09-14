package com.teobaranga.monica.home

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.teobaranga.monica.NavGraphs
import com.teobaranga.monica.appCurrentDestinationAsState
import com.teobaranga.monica.destinations.Destination
import com.teobaranga.monica.startAppDestination

@Composable
fun HomeNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val currentDestination: Destination? by navController.appCurrentDestinationAsState()
    NavigationBar(
        modifier = modifier,
    ) {
        for (homeTab in HomeTab.entries) {
            NavigationBarItem(
                selected = currentDestination?.startAppDestination == homeTab.destination,
                onClick = {
                    navController.navigate(homeTab.destination) {
                        launchSingleTop = true
                        restoreState = true
                        // make the dashboard screen the last one before exiting
                        popUpTo(NavGraphs.rootDashboard.startRoute.route) {
                            saveState = true
                        }
                    }
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
}
