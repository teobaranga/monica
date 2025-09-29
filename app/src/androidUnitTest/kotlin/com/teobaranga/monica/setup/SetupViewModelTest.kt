package com.teobaranga.monica.setup

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.PrintLogger
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.MONICA_URL
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.data.TokenErrorResponse
import com.teobaranga.monica.data.TokenResponse
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.HttpResponse
import io.mockk.coEvery
import io.mockk.mockk
import java.net.URLEncoder

class SetupViewModelTest : BehaviorSpec(
    {

        Given("logged out user") {
            val component = SetupComponent::class.create()
            val viewModel = component.setupViewModel()(SavedStateHandle())
            val testWorkScheduler = component.testWorkScheduler()
            val monicaApi = component.monicaApi()

            fun uiState() = viewModel.uiState.value

            afterTest {
                component.dataStore().reset()
                testWorkScheduler.clearWork()
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

                And("sign in API success") {

                    coEvery { monicaApi.getAccessToken(any()) }.answers {
                        ApiResponse.of {
                            TokenResponse(
                                accessToken = "access_token",
                                refreshToken = "refresh_token",
                            )
                        }
                    }

                    When("sign in") {
                        viewModel.onSignIn()

                        val code = "123456"
                        viewModel.onAuthorizationCode(code)

                        Then("sign in work is scheduled") {
                            testWorkScheduler.isWorkScheduled(SYNC_WORKER_WORK_NAME) shouldBe true
                        }
                    }
                }

                And("sign in API error") {

                    coEvery { monicaApi.getAccessToken(any()) }.answers {
                        val httpResponse = mockk<HttpResponse> {
                            coEvery { call.bodyNullable(any()) } answers { TokenErrorResponse("Test Error") }
                        }
                        ApiResponse.Failure.Error(httpResponse)
                    }

                    When("sign in") {
                        viewModel.onSignIn()

                        val code = "123456"
                        viewModel.onAuthorizationCode(code)

                        Then("sign in work is not scheduled") {
                            testWorkScheduler.isWorkScheduled(SYNC_WORKER_WORK_NAME) shouldBe false
                        }

                        Then("UI state displays error with server message") {
                            uiState().error shouldBe UiState.Error.ConfigurationError("Test Error")
                        }
                    }
                }

                And("sign in API exception") {

                    coEvery { monicaApi.getAccessToken(any()) }.answers {
                        ApiResponse.exception(Throwable())
                    }

                    When("sign in") {
                        viewModel.onSignIn()

                        val code = "123456"
                        viewModel.onAuthorizationCode(code)

                        Then("sign in work is not scheduled") {
                            testWorkScheduler.isWorkScheduled(SYNC_WORKER_WORK_NAME) shouldBe false
                        }

                        Then("UI state displays generic error") {
                            uiState().error shouldBe UiState.Error.ConfigurationError(null)
                        }
                    }
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
