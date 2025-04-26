package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.ContactComponent
import com.teobaranga.monica.contacts.create
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactResponse
import com.teobaranga.monica.contacts.data.SingleContactResponse
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.contacts.validContact
import com.teobaranga.monica.core.data.remote.DeleteResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.datetime.MonthDay
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
import kotlinx.datetime.LocalDate
import java.time.Month

private val birthdate = Instant.parse("2010-06-01T22:19:44.475Z")

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
                viewModel.uiState.first()

                viewModel.onDelete()

                Then("nothing happens") {
                    coVerify(exactly = 0) { contactApi.deleteContact(any()) }
                }
            }

            When("save") {
                viewModel.uiState.first()

                viewModel.onSave()

                Then("nothing happens") {
                    coVerify(exactly = 0) { contactApi.createContact(request = any()) }
                    coVerify(exactly = 0) { contactApi.updateContact(id = any(), request = any()) }
                }
            }
        }

        Given("valid contact id") {
            val savedStateHandle = SavedStateHandle()
            every { savedStateHandle.toRoute<ContactEditRoute>() } returns
                ContactEditRoute(contactId = validContact.contactId)

            component.gendersDao().upsertGenders(listOf(genderMale, genderFemale))

            And("birthday is blank") {
                contactDao.upsertContacts(listOf(validContact.copy(birthdate = null)))

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
                contactDao.upsertContacts(
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
                            initialBirthday = Birthday.Full(LocalDate.parse("2010-06-01")),
                        )
                    }
                }
            }

            And("birthday is age-based") {
                contactDao.upsertContacts(
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
                            initialBirthday = Birthday.AgeBased(14),
                        )
                    }
                }
            }

            And("birthday is only day and month") {
                contactDao.upsertContacts(
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

            When("delete fails") {
                contactDao.upsertContacts(listOf(validContact))
                coEvery { contactApi.deleteContact(validContact.contactId) } returns
                    ApiResponse.exception(Throwable("Something went wrong"))

                val viewModel = component.contactEditViewModel()(savedStateHandle)
                viewModel.uiState.first()

                viewModel.onDelete()

                Then("contact is marked as deleted") {
                    coVerify { contactApi.deleteContact(validContact.contactId) }
                    contactDao.getContact(validContact.contactId).first().syncStatus shouldBe SyncStatus.DELETED
                }
            }

            When("delete completes successfully") {
                contactDao.upsertContacts(listOf(validContact))
                coEvery { contactApi.deleteContact(validContact.contactId) } returns ApiResponse.of {
                    DeleteResponse(deleted = true, id = validContact.contactId)
                }

                val viewModel = component.contactEditViewModel()(savedStateHandle)
                viewModel.uiState.first()

                viewModel.onDelete()

                Then("contact is deleted") {
                    coVerify { contactApi.deleteContact(validContact.contactId) }
                    contactDao.getContacts(listOf(validContact.contactId)).first() shouldBe emptyList()
                }
            }

            When("save fails") {
                contactDao.upsertContacts(listOf(validContact))

                coEvery { contactApi.updateContact(validContact.contactId, any()) } returns
                    ApiResponse.exception(Throwable("Something went wrong"))

                val viewModel = component.contactEditViewModel()(savedStateHandle)
                viewModel.uiState.first()

                viewModel.onSave()

                Then("contact is marked as edited") {
                    coVerify { contactApi.updateContact(validContact.contactId, any()) }
                    contactDao.getContact(validContact.contactId).first().syncStatus shouldBe SyncStatus.EDITED
                }
            }

            When("save succeeds") {
                contactDao.upsertContacts(listOf(validContact))

                coEvery { contactApi.updateContact(validContact.contactId, any()) } returns
                    ApiResponse.of {
                        SingleContactResponse(
                            ContactResponse(
                                id = validContact.contactId,
                                firstName = "Jane",
                                lastName = "Doe",
                                completeName = "Jane Doe",
                                initials = "JD",
                                gender = genderFemale.name,
                                info = ContactResponse.Information(
                                    avatar = ContactResponse.Information.Avatar(
                                        url = validContact.avatar.url,
                                        color = validContact.avatar.color,
                                    )
                                ),
                            )
                        )
                    }

                val viewModel = component.contactEditViewModel()(savedStateHandle)
                viewModel.uiState.first()

                viewModel.onSave()

                Then("contact is up to date") {
                    coVerify { contactApi.updateContact(validContact.contactId, any()) }
                    contactDao.getContact(validContact.contactId).first() shouldBe validContact.copy(
                        firstName = "Jane",
                        lastName = "Doe",
                        completeName = "Jane Doe",
                        initials = "JD",
                        nickname = null,
                        genderId = genderFemale.genderId,
                        syncStatus = SyncStatus.UP_TO_DATE,
                    )
                }
            }
        }
    },
)
