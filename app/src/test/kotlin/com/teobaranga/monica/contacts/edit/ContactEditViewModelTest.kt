package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.teobaranga.monica.contacts.ContactComponent
import com.teobaranga.monica.contacts.create
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.newContactEntity
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.datetime.InstantExt.yearsUntilToday
import com.teobaranga.monica.genders.data.genderFemale
import com.teobaranga.monica.genders.data.genderMale
import com.teobaranga.monica.genders.data.toDomain
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.config.DefaultTestConfig
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import java.time.Month

private const val TEST_CONTACT_ID = 123

class ContactEditViewModelTest : BehaviorSpec(
    {
        defaultTestConfig = DefaultTestConfig(coroutineTestScope = true)
        isolationMode = IsolationMode.InstancePerRoot
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val component = ContactComponent::class.create()

        Given("no contact id") {
            val savedStateHandle = SavedStateHandle()
            every { savedStateHandle.toRoute<ContactEditRoute>() } returns ContactEditRoute(contactId = null)

            val viewModel = component.contactEditViewModel()(savedStateHandle)

            Then("state is blank") {

                viewModel.uiState.test {
                    awaitItem() shouldBe ContactEditUiState.Loaded(
                        id = -1,
                        firstName = "",
                        lastName = null,
                        nickname = null,
                        initialGender = null,
                        genders = emptyList(),
                        initialBirthday = null,
                    )
                }
            }

            When("delete") {

                viewModel.onDelete()

                Then("nothing happens") {
                    val contactApi = component.contactApi()
                    coVerify(exactly = 0) { contactApi.deleteContact(any()) }
                }
            }

            When("save") {

                viewModel.onSave()

                Then("nothing happens") {
                    val contactApi = component.contactApi()
                    coVerify(exactly = 0) { contactApi.createContact(request = any()) }
                    coVerify(exactly = 0) { contactApi.updateContact(id = any(), request = any()) }
                }
            }
        }

        Given("valid contact id") {
            val savedStateHandle = SavedStateHandle()
            every { savedStateHandle.toRoute<ContactEditRoute>() } returns ContactEditRoute(contactId = TEST_CONTACT_ID)

            component.gendersDao().upsertGenders(listOf(genderMale, genderFemale))

            And("birthday is blank") {
                component.contactDao().upsertContacts(listOf(validContact.copy(birthdate = null)))

                Then("state is loaded correctly") {

                    val viewModel = component.contactEditViewModel()(savedStateHandle)

                    viewModel.uiState.test {
                        awaitItem() shouldBe ContactEditUiState.Loaded(
                            id = validContact.contactId,
                            firstName = validContact.firstName,
                            lastName = validContact.lastName,
                            nickname = validContact.nickname,
                            initialGender = genderMale.toDomain(),
                            genders = listOf(genderMale.toDomain(), genderFemale.toDomain()),
                            initialBirthday = null,
                        )
                    }
                }
            }

            And("birthday is fully known") {
                component.contactDao().upsertContacts(
                    listOf(
                        validContact.copy(
                            birthdate = ContactEntity.Birthdate(
                                isAgeBased = false,
                                isYearUnknown = false,
                                date = birthdate
                            ),
                        )
                    )
                )

                Then("state is loaded correctly") {

                    val viewModel = component.contactEditViewModel()(savedStateHandle)

                    viewModel.uiState.test {
                        awaitItem() shouldBe ContactEditUiState.Loaded(
                            id = validContact.contactId,
                            firstName = validContact.firstName,
                            lastName = validContact.lastName,
                            nickname = validContact.nickname,
                            initialGender = genderMale.toDomain(),
                            genders = listOf(genderMale.toDomain(), genderFemale.toDomain()),
                            initialBirthday = Birthday.Full(birthdate),
                        )
                    }
                }
            }

            And("birthday is age-based") {
                component.contactDao().upsertContacts(
                    listOf(
                        validContact.copy(
                            birthdate = ContactEntity.Birthdate(
                                isAgeBased = true,
                                isYearUnknown = false,
                                date = birthdate,
                            ),
                        )
                    )
                )

                Then("state is loaded correctly") {

                    val viewModel = component.contactEditViewModel()(savedStateHandle)

                    viewModel.uiState.test {
                        awaitItem() shouldBe ContactEditUiState.Loaded(
                            id = validContact.contactId,
                            firstName = validContact.firstName,
                            lastName = validContact.lastName,
                            nickname = validContact.nickname,
                            initialGender = genderMale.toDomain(),
                            genders = listOf(genderMale.toDomain(), genderFemale.toDomain()),
                            initialBirthday = Birthday.AgeBased(birthdate.yearsUntilToday()),
                        )
                    }
                }
            }

            And("birthday is only day and month") {
                component.contactDao().upsertContacts(
                    listOf(
                        validContact.copy(
                            birthdate = ContactEntity.Birthdate(
                                isAgeBased = false,
                                isYearUnknown = true,
                                date = birthdate,
                            ),
                        )
                    )
                )

                Then("state is loaded correctly") {

                    val viewModel = component.contactEditViewModel()(savedStateHandle)

                    viewModel.uiState.test {
                        awaitItem() shouldBe ContactEditUiState.Loaded(
                            id = validContact.contactId,
                            firstName = validContact.firstName,
                            lastName = validContact.lastName,
                            nickname = validContact.nickname,
                            initialGender = genderMale.toDomain(),
                            genders = listOf(genderMale.toDomain(), genderFemale.toDomain()),
                            initialBirthday = Birthday.UnknownYear(MonthDay.of(Month.JUNE, 1)),
                        )
                    }
                }
            }
        }
    },
)

private val birthdate = Instant.parse("2010-06-01T22:19:44.475Z")

private val validContact = newContactEntity(
    contactId = TEST_CONTACT_ID,
    firstName = "John",
    lastName = "Doe",
    nickname = "Johnny",
    birthdate = null,
    genderId = genderMale.genderId,
)
