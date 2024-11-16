package com.teobaranga.monica.setup

import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import com.teobaranga.monica.auth.AuthorizationModule
import com.teobaranga.monica.data.ApiModule
import com.teobaranga.monica.data.DataStoreModule
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.data.TestDataStore
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.TestDaosModule
import com.teobaranga.monica.settings.tokenStorage
import com.teobaranga.monica.work.TestWorkScheduleModule
import dagger.Component
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.net.URLEncoder
import javax.inject.Singleton

@Component(
    modules = [
        ViewModelModule::class,
        DispatcherModule::class,
        DataStoreModule::class,
        AuthorizationModule::class,
        TestDaosModule::class,
        ApiModule::class,
        TestWorkScheduleModule::class,
    ],
)
@Singleton
interface SetupComponent {
    fun setupViewModel(): SetupViewModel
    fun userDao(): UserDao
    fun dataStore(): TestDataStore
}

class SetupViewModelTest : BehaviorSpec(
    {
        isolationMode = IsolationMode.InstancePerLeaf

        val component = DaggerSetupComponent.create()
        val viewModel = component.setupViewModel()

        afterTest {
            component.dataStore().reset()
        }

        Context("user is not logged in") {
            viewModel.isLoggedIn.test {
                awaitItem() shouldNotBeNull { this shouldBe false }
            }

            Given("a non-http scheme") {

                viewModel.uiState.onServerAddressChanged(TextFieldValue("blah"))
                viewModel.uiState.onClientIdChanged(TextFieldValue("2"))

                When("I sign in") {

                    viewModel.onSignIn()

                    Then("I get an error") {

                        viewModel.setupUri.test {
                            expectNoEvents()
                        }
                    }
                }
            }

            Given("an http scheme") {

                val address = "http://test.com"
                val clientId = "2"
                viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
                viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

                When("I sign in") {

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

            Given("an http scheme with port") {

                val address = "http://test.com:8080"
                val clientId = "2"
                viewModel.uiState.onServerAddressChanged(TextFieldValue(address))
                viewModel.uiState.onClientIdChanged(TextFieldValue(clientId))

                When("I sign in") {

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
        }

        Context("user is logged in") {

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

            viewModel.isLoggedIn.test {
                awaitItem() shouldNotBeNull { this shouldBe true }
            }
        }
    },
)
