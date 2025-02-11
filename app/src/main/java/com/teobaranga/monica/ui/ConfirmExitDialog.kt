package com.teobaranga.monica.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ConfirmExitDialog(
    state: ConfirmExitDialogState,
    text: String,
    positiveText: String,
    negativeText: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    BackHandler(state.shouldConfirm && !state.isConfirming) {
        state.isConfirming = true
    }

    if (state.isConfirming) {
        AlertDialog(
            modifier = modifier,
            icon = icon?.let {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            },
            title = title?.let {
                {
                    Text(
                        text = title,
                    )
                }
            },
            text = {
                Text(
                    text = text,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.isConfirming = false
                    },
                ) {
                    Text(
                        text = positiveText,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        state.isConfirming = false
                        backDispatcher?.onBackPressed()
                    },
                ) {
                    Text(
                        text = negativeText,
                    )
                }
            },
            onDismissRequest = {
                state.isConfirming = false
            },
        )
    }
}

@Stable
class ConfirmExitDialogState {

    var shouldConfirm by mutableStateOf(false)

    var isConfirming by mutableStateOf(false)
}

@Composable
fun rememberConfirmExitDialogState(): ConfirmExitDialogState {
    return remember { ConfirmExitDialogState() }
}
