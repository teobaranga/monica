package com.teobaranga.monica.setup

import androidx.compose.ui.text.input.TextFieldValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UiStateTest {

    @Test
    fun `Fields are not filled by default`() {
        val uiState = UiState()

        assertEquals("", uiState.serverAddress.text)
        assertEquals("", uiState.clientId.text)
        assertEquals("", uiState.clientSecret.text)
    }

    @Test
    fun `Sign in enabled only when all fields are filled`() {
        val uiState = UiState()

        assertFalse(uiState.isSignInEnabled)

        uiState.onServerAddressChanged(TextFieldValue("http://test.com"))

        assertFalse(uiState.isSignInEnabled)

        uiState.onClientIdChanged(TextFieldValue("2"))

        assertFalse(uiState.isSignInEnabled)

        uiState.onClientSecretChanged(TextFieldValue("abc123"))

        assertTrue(uiState.isSignInEnabled)
    }
}
