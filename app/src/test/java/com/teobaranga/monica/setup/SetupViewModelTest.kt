package com.teobaranga.monica.setup

import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import com.teobaranga.monica.MONICA_URL
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.settings.tokenStorage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.net.URLEncoder

class SetupViewModelTest : BehaviorSpec(
    {
        isolationMode = IsolationMode.InstancePerLeaf

        val component = DaggerSetupComponent.create()
        val viewModel = component.setupViewModel()

        afterTest {
            component.dataStore().reset()
        }


        Given("logged out user") {

            viewModel.isLoggedIn.test {
                awaitItem() shouldBe false
            }

            Then("displays default server address, empty client inputs, and disabled sign in") {

                viewModel.uiState.serverAddress.text shouldBe MONICA_URL
                viewModel.uiState.clientId.text shouldBe ""
                viewModel.uiState.clientSecret.text shouldBe ""
                viewModel.uiState.isSignInEnabled shouldBe false
            }

            And("non-http scheme") {

                viewModel.uiState.onServerAddressChanged(TextFieldValue("blah"))
                viewModel.uiState.onClientIdChanged(TextFieldValue("2"))

                When("sign in") {

                    viewModel.onSignIn()

                    Then("no web redirection happens") {

                        viewModel.setupUri.test {
                            expectNoEvents()
                        }
                    }
                }
            }

            And("valid http scheme") {

                val address = "http://test.com"
                val clientId = "2"
                viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
                viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

                When("sign in") {

                    Then("the setup URL is correct") {

                        viewModel.setupUri.test {

                            viewModel.onSignIn()

                            val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
                            awaitItem() shouldBe
                                "$address/oauth/authorize?" +
                                "$PARAM_CLIENT_ID=$clientId&" +
                                "$PARAM_RESPONSE_TYPE=code&" +
                                "$PARAM_REDIRECT_URI=$redirectUri"
                        }
                    }
                }
            }

            And("valid http scheme with port") {

                val address = "http://test.com:8080"
                val clientId = "2"
                viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
                viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

                When("sign in") {

                    Then("the setup URL is correct") {

                        viewModel.setupUri.test {

                            viewModel.onSignIn()

                            val redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8")
                            awaitItem() shouldBe
                                "$address/oauth/authorize?" +
                                "$PARAM_CLIENT_ID=$clientId&" +
                                "$PARAM_RESPONSE_TYPE=code&" +
                                "$PARAM_REDIRECT_URI=$redirectUri"
                        }
                    }
                }
            }

            And("all inputs filled") {

                viewModel.uiState.onClientIdChanged(TextFieldValue("2"))

                viewModel.uiState.onClientSecretChanged(TextFieldValue("abc123"))

                Then("sign in is enabled") {

                    viewModel.uiState.isSignInEnabled shouldBe true
                }
            }
        }

        Given("logged in user") {

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
)
