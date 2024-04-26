package com.teobaranga.monica.setup

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.TestDispatcher
import com.teobaranga.monica.auth.FakeAuthorizationRepository
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.testDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URLEncoder

@OptIn(ExperimentalCoroutinesApi::class)
class SetupViewModelTest {

    private val savedStateHandle = SavedStateHandle()

    private val dispatcher = TestDispatcher()

    private val dataStore by testDataStore()

    private val authorizationRepository = FakeAuthorizationRepository()

    @Test
    fun `Given non-http scheme, When sign-in, Then errors out`() = runTest(UnconfinedTestDispatcher()) {
        val address = "blah"
        val clientId = "2"

        val viewModel = getViewModel()

        val urls = mutableListOf<String>()
        backgroundScope.launch {
            viewModel.setupUri.toList(urls)
        }

        viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
        viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

        viewModel.onSignIn()

        assertEquals(0, urls.size)
    }

    @Test
    fun `Given http scheme, When sign-in, Then emits correct URL`() = runTest(UnconfinedTestDispatcher()) {
        val address = "http://test.com"
        val clientId = "2"

        val viewModel = getViewModel()

        val urls = mutableListOf<String>()
        backgroundScope.launch {
            viewModel.setupUri.toList(urls)
        }

        viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
        viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

        viewModel.onSignIn()

        val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
        assertEquals(
            "$address/oauth/authorize?" +
                "$PARAM_CLIENT_ID=$clientId&" +
                "$PARAM_RESPONSE_TYPE=code&" +
                "$PARAM_REDIRECT_URI=$redirectUri",
            urls[0],
        )
    }

    @Test
    fun `Given http scheme with port, When sign-in, Then emits correct URL`() = runTest(UnconfinedTestDispatcher()) {
        val address = "http://test.com:8080"
        val clientId = "2"

        val viewModel = getViewModel()

        val urls = mutableListOf<String>()
        backgroundScope.launch {
            viewModel.setupUri.toList(urls)
        }

        viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
        viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

        viewModel.onSignIn()

        val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
        assertEquals(
            "$address/oauth/authorize?" +
                "$PARAM_CLIENT_ID=$clientId&" +
                "$PARAM_RESPONSE_TYPE=code&" +
                "$PARAM_REDIRECT_URI=$redirectUri",
            urls[0],
        )
    }

    private fun getViewModel(): SetupViewModel {
        return SetupViewModel(
            savedStateHandle = savedStateHandle,
            dispatcher = dispatcher,
            dataStore = dataStore,
            authorizationRepository = authorizationRepository,
        )
    }
}
