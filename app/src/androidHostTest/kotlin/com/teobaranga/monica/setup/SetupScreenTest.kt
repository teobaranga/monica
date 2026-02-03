package com.teobaranga.monica.setup

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.teobaranga.monica.browser.LocalWebBrowser
import com.teobaranga.monica.browser.PlatformWebBrowser
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.test.setupAndroidContextProvider
import io.ktor.http.encodeURLParameter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import monica.app.generated.resources.Res
import monica.app.generated.resources.insecure_alert_body1
import monica.app.generated.resources.insecure_alert_body2
import monica.app.generated.resources.insecure_alert_title
import monica.app.generated.resources.setup_client_id_label
import monica.app.generated.resources.setup_client_secret_label
import monica.app.generated.resources.setup_server_address_label
import monica.app.generated.resources.setup_sign_in
import org.jetbrains.compose.resources.getString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class SetupScreenTest {

    val component = SetupComponent::class.create()
    val savedStateHandle = SavedStateHandle()
    val navigator = mockk<NavHostController>()
    val webBrowser = mockk<PlatformWebBrowser>()

    @Before
    fun setup() {
        setupAndroidContextProvider()
    }

    @After
    fun tearDown() {
        component.dataStore().reset()
    }

    @Test
    fun `Given insecure url, When sign in, Then display insecure alert`() = runAndroidComposeUiTest<ComponentActivity> {
        val viewModel = component.setupViewModel()(savedStateHandle)

        setContent {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
                LocalWebBrowser provides webBrowser,
            ) {
                Setup(
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithText(getString(Res.string.setup_server_address_label))
            .performTextReplacement("http://test.com")
        onNodeWithText(getString(Res.string.setup_client_id_label))
            .performTextReplacement("2")
        onNodeWithText(getString(Res.string.setup_client_secret_label))
            .performTextReplacement("abc123")

        onNodeWithText(getString(Res.string.setup_sign_in))
            .performClick()

        // substring because we have an icon inlined
        onNodeWithText(getString(Res.string.insecure_alert_title), substring = true)
            .assertIsDisplayed()
        onNodeWithText(getString(Res.string.insecure_alert_body1))
            .assertIsDisplayed()
        onNodeWithText(getString(Res.string.insecure_alert_body2))
            .assertIsDisplayed()
    }

    @Test
    fun `Given secure url, When sign in, Then launch web browser`() = runAndroidComposeUiTest<ComponentActivity> {
        every { webBrowser.open(any()) } answers { }

        val viewModel = component.setupViewModel()(savedStateHandle)

        setContent {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
                LocalWebBrowser provides webBrowser,
            ) {
                Setup(
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithText(getString(Res.string.setup_server_address_label))
            .performTextReplacement("https://test.com")
        onNodeWithText(getString(Res.string.setup_client_id_label))
            .performTextReplacement("2")
        onNodeWithText(getString(Res.string.setup_client_secret_label))
            .performTextReplacement("abc123")

        onNodeWithText(getString(Res.string.setup_sign_in))
            .performClick()

        verify {
            webBrowser.open(buildString {
                append("https://test.com/oauth/authorize?")
                append("client_id=2&")
                append("response_type=code&")
                append("redirect_uri=${"https://monica.teobaranga.com/callback".encodeURLParameter()}")
            })
        }
    }
}
