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
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun HomeNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val currentDestination: DestinationSpec? by navController.currentDestinationAsState()
    NavigationBar(
        modifier = modifier,
    ) {
        for (homeTab in HomeTab.entries) {
            NavigationBarItem(
                selected = homeTab.destination.destinations.contains(currentDestination?.startDestination),
                onClick = {
                    navController.navigate(homeTab.destination.route) {
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
