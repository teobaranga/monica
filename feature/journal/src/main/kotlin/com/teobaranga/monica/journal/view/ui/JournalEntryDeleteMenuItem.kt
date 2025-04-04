package com.teobaranga.monica.journal.view.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teobaranga.monica.core.ui.OverflowMenuScope

@Composable
fun OverflowMenuScope.JournalEntryDeleteMenuItem(onDelete: () -> Unit, modifier: Modifier = Modifier) {
    var isConfirming by remember { mutableStateOf(false) }
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Text(
                text = "Delete",
            )
        },
        onClick = {
            isConfirming = true
        },
    )
    if (isConfirming) {
        AlertDialog(
            onDismissRequest = {
                isConfirming = false
                dismissMenu()
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this journal entry?",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dismissMenu()
                        onDelete()
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isConfirming = false
                        dismissMenu()
                    },
                ) {
                    Text(
                        text = "Cancel",
                    )
                }
            },
        )
    }
}
