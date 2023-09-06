package com.teobaranga.monica.setup

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class UiState {

    var serverAddress by mutableStateOf(TextFieldValue())

    var clientId by mutableStateOf(TextFieldValue())

    var clientSecret by mutableStateOf(TextFieldValue())

    val isSignInEnabled by derivedStateOf {
        serverAddress.text.isNotBlank() && clientId.text.isNotBlank() && clientSecret.text.isNotBlank()
    }

    companion object {
        val Saver = listSaver<UiState, Any>(
            save = { uiState ->
                TextFieldValue.Saver.run {
                    listOfNotNull(
                        save(uiState.serverAddress),
                        save(uiState.clientId),
                        save(uiState.clientSecret),
                    )
                }
            },
            restore = { saveables ->
                val serverAddress = saveables.getOrNull(0)?.let {
                    TextFieldValue.Saver.restore(it)
                }
                val clientId = saveables.getOrNull(1)?.let {
                    TextFieldValue.Saver.restore(it)
                }
                val clientSecret = saveables.getOrNull(2)?.let {
                    TextFieldValue.Saver.restore(it)
                }
                UiState().apply {
                    if (serverAddress != null) {
                        this.serverAddress = serverAddress
                    }
                    if (clientId != null) {
                        this.clientId = clientId
                    }
                    if (clientSecret != null) {
                        this.clientSecret = clientSecret
                    }
                }
            }
        )
    }
}
