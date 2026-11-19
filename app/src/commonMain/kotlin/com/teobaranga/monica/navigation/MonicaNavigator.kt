package com.teobaranga.monica.navigation

import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.core.ui.navigation.Navigator

/**
 * [Navigator] backed by the app's [MonicaNavigationState].
 */
class MonicaNavigator(
    private val navigationState: MonicaNavigationState,
) : Navigator {

    override fun navigate(route: NavKey) {
        if (route in navigationState.backStacks) {
            navigationState.topLevelRoute = route
        } else {
            navigationState.currentBackStack.add(route)
        }
    }

    override fun popBackStack() {
        val backStack = navigationState.currentBackStack
        when {
            backStack.size > 1 -> backStack.removeLast()
            navigationState.topLevelRoute != navigationState.startRoute -> {
                navigationState.topLevelRoute = navigationState.startRoute
            }
        }
    }
}
