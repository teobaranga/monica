package com.teobaranga.monica.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Immutable
interface OverflowMenuScope : ColumnScope {

    fun dismissMenu()
}

private class OverflowMenuScopeImpl(
    columnScope: ColumnScope,
    private val dismissMenu: () -> Unit,
) : OverflowMenuScope,
    ColumnScope by columnScope {

    override fun dismissMenu() {
        dismissMenu.invoke()
    }
}

@Composable
fun OverflowMenu(modifier: Modifier = Modifier, content: @Composable OverflowMenuScope.() -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    IconButton(
        modifier = modifier,
        onClick = {
            menuExpanded = !menuExpanded
        },
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "More",
        )
    }
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = {
            menuExpanded = false
        },
        content = {
            val overflowMenuScope = remember {
                OverflowMenuScopeImpl(
                    columnScope = this,
                    dismissMenu = {
                        menuExpanded = false
                    },
                )
            }
            with(overflowMenuScope) {
                content()
            }
        },
    )
}
