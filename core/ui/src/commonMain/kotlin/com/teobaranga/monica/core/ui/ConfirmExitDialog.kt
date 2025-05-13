package com.teobaranga.monica.core.ui

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfirmExitDialog(
    state: ConfirmExitDialogState,
    text: String,
    positiveText: String,
    negativeText: String,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
) {
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
                        onExit()
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
