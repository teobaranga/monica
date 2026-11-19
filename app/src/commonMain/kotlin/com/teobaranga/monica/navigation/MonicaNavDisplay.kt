package com.teobaranga.monica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

/**
 * Renders the navigation [entries] using the platform's preferred transitions.
 *
 * Top level destinations switch between each other using transitions that make sense on the
 * platform, while navigating deeper into a stack uses the platform's push and pop transitions.
 *
 * @param entries the entries to render.
 * @param topLevelContentKeys the [NavEntry.contentKey] of each top level destination. Navigating
 *   between two of them is treated as a tab switch rather than a push.
 * @param onBack called when the system back gesture is performed.
 * @param modifier the modifier to apply to the display.
 */
@Composable
internal expect fun MonicaNavDisplay(
    entries: List<NavEntry<NavKey>>,
    topLevelContentKeys: Set<Any>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
)
