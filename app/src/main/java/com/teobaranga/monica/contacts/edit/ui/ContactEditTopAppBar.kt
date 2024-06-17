package com.teobaranga.monica.contacts.edit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.ui.OverflowMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactEditTopAppBar(onBack: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
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
            OverflowMenu {
                ContactEditDeleteMenuItem(
                    onDelete = onDelete,
                )
            }
        },
    )
}
