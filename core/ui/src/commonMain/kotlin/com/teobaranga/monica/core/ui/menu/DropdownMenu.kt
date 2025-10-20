package com.teobaranga.monica.core.ui.menu

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.annotation.RememberInComposition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * Simple dropdown menu that reduces boilerplate, particularly around state management.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    state: DropdownMenuState,
    anchor: @Composable ExposedDropdownMenuBoxScope.() -> Unit,
    menuItems: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = state.expanded,
        onExpandedChange = {
            state.expanded = it
        },
    ) {
        anchor()

        ExposedDropdownMenu(
            expanded = state.expanded,
            onDismissRequest = {
                state.expanded = false
            },
            matchAnchorWidth = false,
            content = menuItems,
        )
    }
}

@Stable
class DropdownMenuState @RememberInComposition constructor(expanded: Boolean) {

    var expanded by mutableStateOf(expanded)
}

@Composable
fun rememberDropdownMenuState(expanded: Boolean = false): DropdownMenuState {
    return remember { DropdownMenuState(expanded) }
}
