package com.teobaranga.monica.setup

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.teobaranga.monica.MONICA_URL

@Stable
class UiState {

    sealed interface Error {
        data object ConfigurationError : Error
    }

    var serverAddress by mutableStateOf(TextFieldValue(MONICA_URL))
        private set

    var clientId by mutableStateOf(TextFieldValue())
        private set

    var clientSecret by mutableStateOf(TextFieldValue())
        private set

    var isSigningIn by mutableStateOf(false)

    val isSignInEnabled by derivedStateOf {
        serverAddress.text.isNotBlank() &&
            clientId.text.isNotBlank() &&
            clientSecret.text.isNotBlank() &&
            !isSigningIn
    }

    var error: Error? by mutableStateOf(null)

    fun onServerAddressChanged(serverAddress: TextFieldValue) {
        if (this.serverAddress.text != serverAddress.text) {
            error = null
        }
        this.serverAddress = serverAddress
    }

    fun onClientIdChanged(clientId: TextFieldValue) {
        if (this.clientId.text != clientId.text) {
            error = null
        }
        this.clientId = clientId
    }

    fun onClientSecretChanged(clientSecret: TextFieldValue) {
        if (this.clientSecret.text != clientSecret.text) {
            error = null
        }
        this.clientSecret = clientSecret
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
            },
        )
    }
}
