package com.teobaranga.monica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay

@Composable
internal actual fun MonicaNavDisplay(
    entries: List<NavEntry<NavKey>>,
    topLevelContentKeys: Set<Any>,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    NavDisplay(
        entries = entries,
        onBack = onBack,
        modifier = modifier,
    )
}
