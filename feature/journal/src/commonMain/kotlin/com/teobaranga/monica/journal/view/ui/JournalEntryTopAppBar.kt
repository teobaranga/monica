package com.teobaranga.monica.journal.view.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.core.ui.OverflowMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryTopAppBar(
    onBack: () -> Unit,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        title = {
            // No title
        },
        actions = {
            if (onDelete != null) {
                OverflowMenu {
                    JournalEntryDeleteMenuItem(
                        onDelete = onDelete,
                    )
                }
            }
        },
    )
}
