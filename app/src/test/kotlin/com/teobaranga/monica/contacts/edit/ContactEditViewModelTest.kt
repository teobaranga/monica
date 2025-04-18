package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.ContactComponent
import com.teobaranga.monica.contacts.create
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.newContactEntity
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.data.remote.DeleteResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.datetime.InstantExt.yearsUntilToday
import com.teobaranga.monica.genders.data.genderFemale
import com.teobaranga.monica.genders.data.genderMale
import com.teobaranga.monica.genders.data.toDomain
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.config.DefaultTestConfig
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import java.time.Month

private const val TEST_CONTACT_ID = 123

class ContactEditViewModelTest : BehaviorSpec(
    {
        defaultTestConfig = DefaultTestConfig(coroutineTestScope = true)

        val component = ContactComponent::class.create()
        val contactApi = component.contactApi()
        val contactDao = component.contactDao()

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

            When("delete completes successfully") {
                component.contactDao().upsertContacts(listOf(validContact))
                coEvery { contactApi.deleteContact(TEST_CONTACT_ID) } returns ApiResponse.of {
                    DeleteResponse(deleted = true, id = TEST_CONTACT_ID)
                }

                val viewModel = component.contactEditViewModel()(savedStateHandle)

                viewModel.onDelete()

                Then("contact is deleted") {
                    coVerify { contactApi.deleteContact(TEST_CONTACT_ID) }
                    contactDao.getContacts(listOf(TEST_CONTACT_ID)).first() shouldBe emptyList()
                }
            }

            When("delete fails") {
                component.contactDao().upsertContacts(listOf(validContact))
                coEvery { contactApi.deleteContact(TEST_CONTACT_ID) } returns
                    ApiResponse.exception(Throwable("Something went wrong"))

                val viewModel = component.contactEditViewModel()(savedStateHandle)

                viewModel.onDelete()

                Then("contact is marked as deleted") {
                    coVerify { contactApi.deleteContact(TEST_CONTACT_ID) }
                    contactDao.getContact(TEST_CONTACT_ID).first().syncStatus shouldBe SyncStatus.DELETED
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
