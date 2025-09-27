package com.teobaranga.monica.setup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.MONICA_URL

@Stable
class UiState(
    val serverAddress: TextFieldState = TextFieldState(MONICA_URL),
    val clientId: TextFieldState = TextFieldState(),
    val clientSecret: TextFieldState = TextFieldState(),
) {

    sealed interface Error {

        data object ServerProtocolError : Error

        data class ConfigurationError(val message: String?) : Error
    }

    var isSigningIn by mutableStateOf(false)

    val isSignInEnabled by derivedStateOf {
        serverAddress.text.isNotBlank() &&
            clientId.text.isNotBlank() &&
            clientSecret.text.isNotBlank() &&
            !isSigningIn
    }

    var error by mutableStateOf<Error?>(null)
}
