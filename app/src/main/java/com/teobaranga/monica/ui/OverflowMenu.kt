package com.teobaranga.monica.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun OverflowMenu(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
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
        content = content,
    )
}
