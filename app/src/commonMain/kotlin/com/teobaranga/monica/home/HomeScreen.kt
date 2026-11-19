package com.teobaranga.monica.home

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.contacts.ContactsRoute
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.dashboard.DashboardRoute
import com.teobaranga.monica.journal.list.JournalEntriesRoute
import com.teobaranga.monica.navigation.MonicaNavDisplay
import com.teobaranga.monica.navigation.MonicaNavigationState
import com.teobaranga.monica.navigation.MonicaNavigator
import com.teobaranga.monica.navigation.monicaEntryProvider
import com.teobaranga.monica.ui.navigation.TopLevelRoute
import com.teobaranga.monica.util.compose.thenIf

val topLevelRoutes = listOf(
    TopLevelRoute("Dashboard", DashboardRoute, Icons.Default.Dashboard),
    TopLevelRoute("Contacts", ContactsRoute, Icons.Default.Groups),
    TopLevelRoute("Journal", JournalEntriesRoute, Icons.AutoMirrored.Filled.MenuBook),
)

@Composable
fun HomeScreen(
    navigationState: MonicaNavigationState,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current
    val entryProvider = remember {
        monicaEntryProvider()
    }
    val topLevelContentKeys = remember(entryProvider) {
        topLevelRoutes.map { topLevelRoute -> entryProvider(topLevelRoute.route).contentKey }.toSet()
    }
    val isTopLevelDestination = navigationState.currentBackStack.size == 1
    Scaffold(
        modifier = modifier,
        bottomBar = {
            HomeNavigationBar(
                modifier = Modifier
                    .animateContentSize()
                    .thenIf(!isTopLevelDestination) {
                        height(0.dp)
                    },
                selectedRoute = navigationState.topLevelRoute,
                onNavigate = navigator::navigate,
            )
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        if (!LocalInspectionMode.current) {
            MonicaNavDisplay(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding),
                entries = navigationState.toDecoratedEntries(entryProvider),
                topLevelContentKeys = topLevelContentKeys,
                onBack = navigator::popBackStack,
            )
        }
    }
}

@Composable
fun HomeNavigationBar(
    selectedRoute: NavKey,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
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
                selected = topLevelRoute.route == selectedRoute,
                onClick = {
                    onNavigate(topLevelRoute.route)
                },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    MonicaTheme {
        val navigationState = remember {
            MonicaNavigationState(
                startRoute = DashboardRoute,
                topLevelRoute = mutableStateOf(DashboardRoute),
                backStacks = topLevelRoutes.associate { topLevelRoute ->
                    topLevelRoute.route to NavBackStack(topLevelRoute.route)
                },
            )
        }
        CompositionLocalProvider(
            LocalNavigator provides remember(navigationState) {
                MonicaNavigator(navigationState)
            },
        ) {
            HomeScreen(
                navigationState = navigationState,
            )
        }
    }
}
