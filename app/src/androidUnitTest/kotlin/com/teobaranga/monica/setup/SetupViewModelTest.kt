package com.teobaranga.monica.setup

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.PrintLogger
import com.teobaranga.monica.MONICA_URL
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.data.user.MeEntity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.net.URLEncoder

class SetupViewModelTest : BehaviorSpec(
    {

        Given("logged out user") {
            val component = SetupComponent::class.create()
            val viewModel = component.setupViewModel()(SavedStateHandle())

            fun uiState() = viewModel.uiState.value

            afterTest {
                component.dataStore().reset()
            }

            viewModel.isLoggedIn.test {
                awaitItem() shouldBe false
            }

            Then("displays default server address, empty client inputs, and disabled sign in") {

                uiState().serverAddress.text shouldBe MONICA_URL
                uiState().clientId.text shouldBe ""
                uiState().clientSecret.text shouldBe ""
                uiState().isSignInEnabled shouldBe false
            }

            And("non-http scheme") {

                uiState().serverAddress.setTextAndPlaceCursorAtEnd("blah://test.com")
                uiState().clientId.setTextAndPlaceCursorAtEnd("2")

                When("sign in") {

                    viewModel.onSignIn()

                    Then("no web redirection happens") {
                        viewModel.setupEvents.test {
                            expectNoEvents()
                        }
                    }

                    Then("show unsupported protocol error") {
                        uiState().error shouldBe UiState.Error.ServerAddressProtocolError
                    }
                }
            }

            And("valid http scheme") {

                val address = "http://test.com"
                val clientId = "2"
                uiState().serverAddress.setTextAndPlaceCursorAtEnd(address)
                uiState().clientId.setTextAndPlaceCursorAtEnd(clientId)

                When("sign in") {

                    Then("the setup URL is correct") {

                        viewModel.setupEvents.test {

                            viewModel.onSignIn()

                            val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
                            awaitItem() shouldBe SetupEvent.Login(
                                setupUrl = "$address/oauth/authorize?" +
                                        "$PARAM_CLIENT_ID=$clientId&" +
                                        "$PARAM_RESPONSE_TYPE=code&" +
                                        "$PARAM_REDIRECT_URI=$redirectUri",
                                isSecure = false,
                            )
                        }
                    }
                }
            }

            And("valid http scheme with port") {

                val address = "http://test.com:8080"
                val clientId = "2"
                uiState().serverAddress.setTextAndPlaceCursorAtEnd(address)
                uiState().clientId.setTextAndPlaceCursorAtEnd(clientId)

                When("sign in") {

                    Then("the setup URL is correct") {

                        viewModel.setupEvents.test {

                            viewModel.onSignIn()

                            val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
                            awaitItem() shouldBe SetupEvent.Login(
                                setupUrl = "$address/oauth/authorize?" +
                                    "$PARAM_CLIENT_ID=$clientId&" +
                                    "$PARAM_RESPONSE_TYPE=code&" +
                                    "$PARAM_REDIRECT_URI=$redirectUri",
                                isSecure = false,
                            )
                        }
                    }
                }
            }

            And("valid https scheme") {

                val address = "https://test.com"
                val clientId = "2"
                uiState().serverAddress.setTextAndPlaceCursorAtEnd(address)
                uiState().clientId.setTextAndPlaceCursorAtEnd(clientId)

                When("sign in") {

                    Then("the setup URL is correct") {

                        viewModel.setupEvents.test {

                            viewModel.onSignIn()

                            val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
                            awaitItem() shouldBe SetupEvent.Login(
                                setupUrl = "$address/oauth/authorize?" +
                                    "$PARAM_CLIENT_ID=$clientId&" +
                                    "$PARAM_RESPONSE_TYPE=code&" +
                                    "$PARAM_REDIRECT_URI=$redirectUri",
                                isSecure = true,
                            )
                        }
                    }
                }
            }

            And("all inputs filled") {

                uiState().clientId.setTextAndPlaceCursorAtEnd("2")

                uiState().clientSecret.setTextAndPlaceCursorAtEnd("abc123")

                Then("sign in is enabled") {

                    uiState().isSignInEnabled shouldBe true
                }
            }
        }

        Given("logged in user") {
            val component = SetupComponent::class.create()
            val viewModel = component.setupViewModel()(SavedStateHandle())

            afterTest {
                component.dataStore().reset()
            }

            component
                .userDao()
                .upsertMe(MeEntity(id = 0, firstName = "Teo", contactId = null))

            component
                .dataStore()
                .edit {
                    it.tokenStorage {
                        setAuthorizationCode("123456")
                    }
                }

            Then("login state is correct") {

                viewModel.isLoggedIn.test {
                    awaitItem() shouldBe true
                }
            }
        }
    },
) {

    companion object {

        init {
            KmLogging.setLoggers(PrintLogger(FixedLogLevel(true)))
        }
    }
}
