package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactActivityTopAppBar(
    isEdit: Boolean,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = if (!isEdit) {
                    "New activity"
                } else {
                    "Edit activity"
                },
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back",
                )
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            if (isEdit) {
                IconButton(
                    onClick = {
                        showDeleteConfirmDialog = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete activity",
                    )
                }
            }
        },
    )

    if (showDeleteConfirmDialog) {
        DeleteAlertDialog(
            onConfirm = onDelete,
            onCancel = {
                showDeleteConfirmDialog = false
            },
        )
    }
}

@Composable
private fun DeleteAlertDialog(onConfirm: () -> Unit, onCancel: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = "Delete")
        },
        text = {
            Text(text = "Are you sure you want to delete this activity?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
            ) {
                Text(text = "Cancel")
            }
        },
    )
}
