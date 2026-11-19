package com.teobaranga.monica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer

/**
 * Holds one back stack per top level route and remembers which one is currently selected.
 *
 * Every top level route keeps its own stack so that switching tabs preserves the state of each
 * tab, and all of them are saved across configuration changes and process death.
 */
@Stable
class MonicaNavigationState internal constructor(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {

    var topLevelRoute: NavKey by topLevelRoute

    val currentBackStack: NavBackStack<NavKey>
        get() = checkNotNull(backStacks[topLevelRoute]) {
            "No back stack for top level route $topLevelRoute"
        }

    /**
     * The stacks that make up the current back stack. The start route is always kept underneath
     * the selected route so that navigating back from a top level route returns to it.
     */
    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }

    /**
     * Decorates the entries of each back stack and flattens the stacks that are currently in use into
     * the list rendered by the [androidx.navigation3.ui.NavDisplay].
     */
    @Composable
    fun toDecoratedEntries(
        entryProvider: (NavKey) -> NavEntry<NavKey>,
    ): List<NavEntry<NavKey>> {
        val decoratedBackStacks = backStacks.mapValues { (_, backStack) ->
            rememberDecoratedNavEntries(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider,
            )
        }
        return stacksInUse.flatMap { route ->
            decoratedBackStacks[route].orEmpty()
        }
    }
}

/**
 * Creates a [MonicaNavigationState] that survives configuration changes and process death.
 */
@Composable
fun rememberMonicaNavigationState(
    configuration: SavedStateConfiguration,
    startRoute: NavKey,
    topLevelRoutes: List<NavKey>,
): MonicaNavigationState {
    val topLevelRoute = rememberSerializable(
        configuration = configuration,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        mutableStateOf(startRoute)
    }
    val backStacks = topLevelRoutes.associateWith { route ->
        rememberNavBackStack(configuration, route)
    }
    return remember(startRoute, backStacks) {
        MonicaNavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks,
        )
    }
}
