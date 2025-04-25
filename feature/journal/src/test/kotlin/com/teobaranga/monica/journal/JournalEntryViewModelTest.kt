package com.teobaranga.monica.journal

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.journal.data.remote.JournalEntryCreateRequest
import com.teobaranga.monica.journal.data.remote.JournalEntryResponse
import com.teobaranga.monica.journal.view.JournalEntryRoute
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.config.DefaultTestConfig
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn

class JournalEntryViewModelTest : BehaviorSpec(
    {
        defaultTestConfig = DefaultTestConfig(coroutineTestScope = true)

        val component = TestJournalComponent::class.create()
        val api = component.api()
        val dao = component.dao()

        Given("no entry id") {
            val savedStateHandle = SavedStateHandle()
            every { savedStateHandle.toRoute<JournalEntryRoute>() } returns JournalEntryRoute(entryId = null)

            val viewModel = component.journalEntryViewModel()(savedStateHandle)

            Then("state is blank") {

                viewModel.uiState.test {
                    awaitItem() shouldBe JournalEntryUiState.Loaded(
                        id = -1,
                        initialTitle = null,
                        initialPost = "",
                        initialDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                    )
                }
            }

            When("delete") {
                viewModel.uiState.first()

                viewModel.onDelete()

                Then("nothing happens") {
                    coVerify(exactly = 0) { api.deleteJournalEntry(any()) }
                }
            }

            When("save") {
                viewModel.uiState.first()

                viewModel.onSave()

                Then("nothing happens") {
                    coVerify(exactly = 0) { api.createJournalEntry(request = any()) }
                    coVerify(exactly = 0) { api.updateJournalEntry(id = any(), request = any()) }
                }
            }
        }

        Given("valid entry id") {
            val savedStateHandle = SavedStateHandle()
            every { savedStateHandle.toRoute<JournalEntryRoute>() } returns
                JournalEntryRoute(entryId = validJournalEntry.id)

            dao.upsertJournalEntry(validJournalEntry)

            Then("state is loaded correctly") {

                val viewModel = component.journalEntryViewModel()(savedStateHandle)

                viewModel.uiState.test {
                    awaitItem() shouldBe JournalEntryUiState.Loaded(
                        id = validJournalEntry.id,
                        initialTitle = validJournalEntry.title,
                        initialPost = validJournalEntry.post,
                        initialDate = validJournalEntry.date,
                    )
                }
            }

            When("date is changed") {
                val testDate = LocalDate(2023, Month.APRIL, 27)

                coEvery {
                    api.updateJournalEntry(
                        id = validJournalEntry.id,
                        request = JournalEntryCreateRequest(
                            title = validJournalEntry.title,
                            post = validJournalEntry.post,
                            date = testDate,
                        )
                    )
                } returns ApiResponse.of {
                    JournalEntryResponse(
                        data = validJournalEntry.toResponse(
                            date = testDate.atStartOfDayIn(TimeZone.UTC),
                        )
                    )
                }

                val viewModel = component.journalEntryViewModel()(savedStateHandle)
                val uiState = viewModel.uiState.first() as JournalEntryUiState.Loaded
                uiState.date = testDate

                viewModel.onSave()

                Then("date is updated") {

                    // Remote
                    coVerify {
                        api.updateJournalEntry(
                            id = validJournalEntry.id,
                            request = JournalEntryCreateRequest(
                                title = validJournalEntry.title,
                                post = validJournalEntry.post,
                                date = testDate,
                            )
                        )
                    }

                    // Local
                    val updatedEntry = dao.getJournalEntry(validJournalEntry.id).first()
                    updatedEntry.date shouldBe testDate
                }
            }
        }
    },
)
