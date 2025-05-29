package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.ContactComponent
import com.teobaranga.monica.contacts.create
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.SingleContactResponse
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.toCreateRequest
import com.teobaranga.monica.contacts.toResponse
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.contacts.validContact
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.datetime.di.testNow
import com.teobaranga.monica.datetime.di.testTimeZone
import com.teobaranga.monica.genders.data.genderFemale
import com.teobaranga.monica.genders.data.genderMale
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.config.DefaultTestConfig
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.Month

class ContactBirthdayTest : BehaviorSpec(
    {
        defaultTestConfig = DefaultTestConfig(coroutineTestScope = true)

        val component = ContactComponent::class.create()
        val contactApi = component.contactApi()
        val contactDao = component.contactDao()
        val gendersDao = component.gendersDao()
        val savedStateHandle = SavedStateHandle()

        beforeContainer {
            every { savedStateHandle.toRoute<ContactEditRoute>() } returns
                ContactEditRoute(contactId = validContact.contactId)
            gendersDao.upsertGenders(listOf(genderMale, genderFemale))
            contactDao.upsertContacts(listOf(validContact.copy(birthdate = null)))
        }

        Given("unknown year birthday") {
            val expectedBirthday = Instant.parse("${testNow.toLocalDateTime(testTimeZone).year}-06-01T00:00:00.000Z")
            coEvery {
                contactApi.updateContact(
                    id = validContact.contactId,
                    request = validContact.toCreateRequest(
                        birthdayDay = 1,
                        birthdayMonth = Month.JUNE.value,
                        birthdayYear = null,
                        isBirthdateKnown = true,
                    ),
                )
            } returns
                ApiResponse.of {
                    SingleContactResponse(
                        validContact.toResponse(
                            gender = genderMale.name,
                            birthdateIsAgeBased = false,
                            birthdateIsYearUnknown = true,
                            birthday = expectedBirthday,
                        )
                    )
                }

            val viewModel = component.contactEditViewModel()(savedStateHandle)
            val uiState = viewModel.uiState.first() as ContactEditUiState.Loaded
            uiState.birthday = Birthday.UnknownYear(MonthDay.of(Month.JUNE, 1))

            When("saving") {

                viewModel.onSave()

                Then("contact has correct birthday") {

                    val updatedContact = contactDao.getContact(validContact.contactId).first()
                    updatedContact.birthdate shouldBe ContactEntity.Birthdate(
                        isAgeBased = false,
                        isYearUnknown = true,
                        date = expectedBirthday,
                    )
                }
            }
        }

        Given("age based birthday") {
            val currentYear = testNow.toLocalDateTime(testTimeZone).year
            val age = 18
            val birthdayYear = currentYear - age
            val expectedBirthday = Instant.parse("${birthdayYear}-01-01T00:00:00.000Z")
            coEvery {
                contactApi.updateContact(
                    id = validContact.contactId,
                    request = validContact.toCreateRequest(
                        birthdayYear = birthdayYear,
                        isBirthdateKnown = true,
                        birthdateAge = age,
                        birthdateIsAgeBased = true,
                    ),
                )
            } returns
                ApiResponse.of {
                    SingleContactResponse(
                        validContact.toResponse(
                            gender = genderMale.name,
                            birthdateIsAgeBased = true,
                            birthdateIsYearUnknown = false,
                            birthday = expectedBirthday,
                        )
                    )
                }

            val viewModel = component.contactEditViewModel()(savedStateHandle)
            val uiState = viewModel.uiState.first() as ContactEditUiState.Loaded
            uiState.birthday = Birthday.AgeBased(age)

            When("saving") {

                viewModel.onSave()

                Then("contact has correct birthday") {

                    val updatedContact = contactDao.getContact(validContact.contactId).first()
                    updatedContact.birthdate shouldBe ContactEntity.Birthdate(
                        isAgeBased = true,
                        isYearUnknown = false,
                        date = expectedBirthday,
                    )
                }
            }
        }

        Given("exact birthday") {
            val expectedBirthday = Instant.parse("1995-02-01T00:00:00.000Z")
            coEvery {
                contactApi.updateContact(
                    id = validContact.contactId,
                    request = validContact.toCreateRequest(
                        isBirthdateKnown = true,
                        birthdayDay = 1,
                        birthdayMonth = 2,
                        birthdayYear = 1995,
                    ),
                )
            } returns
                ApiResponse.of {
                    SingleContactResponse(
                        validContact.toResponse(
                            gender = genderMale.name,
                            birthday = expectedBirthday,
                        )
                    )
                }

            val viewModel = component.contactEditViewModel()(savedStateHandle)
            val uiState = viewModel.uiState.first() as ContactEditUiState.Loaded
            uiState.birthday = Birthday.Full(LocalDate.parse("1995-02-01"))

            When("saving") {

                viewModel.onSave()

                Then("contact has correct birthday") {

                    val updatedContact = contactDao.getContact(validContact.contactId).first()
                    updatedContact.birthdate shouldBe ContactEntity.Birthdate(
                        isAgeBased = false,
                        isYearUnknown = false,
                        date = expectedBirthday,
                    )
                }
            }
        }
    },
)
