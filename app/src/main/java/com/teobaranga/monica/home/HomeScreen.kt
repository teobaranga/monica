package com.teobaranga.monica.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.contacts.ContactsRoute
import com.teobaranga.monica.dashboard.DashboardRoute
import com.teobaranga.monica.journal.list.JournalEntriesRoute
import com.teobaranga.monica.setup.SetupRoute
import com.teobaranga.monica.core.ui.preview.PreviewPixel4
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.ui.navigation.TopLevelRoute
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.thenIf

val topLevelRoutes = listOf(
    TopLevelRoute("Dashboard", DashboardRoute, Icons.Default.Dashboard),
    TopLevelRoute("Contacts", ContactsRoute, Icons.Default.Groups),
    TopLevelRoute("Journal", JournalEntriesRoute, Icons.AutoMirrored.Filled.MenuBook),
)

@Composable
internal fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = injectedViewModel(),
) {
    val navigator = LocalNavigator.current
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == false) {
            navigator.navigate(SetupRoute) {
                popUpTo(HomeRoute) {
                    inclusive = true
                }
            }
        }
    }

    if (isLoggedIn == true) {
        HomeScreen(
            modifier = modifier,
        )
    } else {
        // TODO Replace with Loading screen
        Box(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val isVisible = topLevelRoutes.any { currentDestination?.hasRoute(it.route::class) == true }
            HomeNavigationBar(
                modifier = Modifier
                    .animateContentSize()
                    .thenIf(!isVisible) {
                        height(0.dp)
                    },
                navController = navController,
            )
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        if (!LocalInspectionMode.current) {
            CompositionLocalProvider(
                LocalNavigator provides navController,
            ) {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .consumeWindowInsets(contentPadding),
                    navController = navController,
                    startDestination = topLevelRoutes[0].route,
                    builder = HomeNavGraphBuilder,
                )
            }
        }
    }
}

@Composable
fun HomeNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = topLevelRoute.icon,
                        contentDescription = topLevelRoute.name,
                    )
                },
                label = {
                    Text(
                        text = topLevelRoute.name,
                    )
                },
                selected = currentDestination?.hierarchy?.any { navDestination ->
                    navDestination.hasRoute(topLevelRoute.route::class)
                } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@PreviewPixel4
@Composable
private fun PreviewHomeScreen() {
    MonicaTheme {
        HomeScreen()
    }
}
