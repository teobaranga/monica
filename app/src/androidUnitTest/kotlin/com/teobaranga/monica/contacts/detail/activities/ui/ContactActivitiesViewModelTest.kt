package com.teobaranga.monica.contacts.detail.activities.ui

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.activities.data.ContactActivitiesResponse
import com.teobaranga.monica.contacts.ContactComponent
import com.teobaranga.monica.contacts.TEST_CONTACT_ID
import com.teobaranga.monica.contacts.create
import com.teobaranga.monica.core.data.remote.MetaResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.config.DefaultTestConfig
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.engine.coroutines.backgroundScope
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ContactActivitiesViewModelTest : BehaviorSpec(
    {
        defaultTestConfig = DefaultTestConfig(coroutineTestScope = true)

        val component = ContactComponent::class.create()
        val viewModel = component.contactActivitiesViewModel()(TEST_CONTACT_ID)
        val contactApi = component.contactApi()

        Given("activities collected") {

            coEvery { contactApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) } returns ApiResponse.of {
                ContactActivitiesResponse(
                    data = emptyList(),
                    meta = MetaResponse(
                        currentPage = 0,
                        lastPage = 0,
                    )
                )
            }

            val job1 = backgroundScope.launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                viewModel.contactActivities.collect { }
            }

            Then("refresh is triggered") {
                coVerify(exactly = 1) { contactApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) }
            }

            When("off-screen and 5 seconds have passed") {
                job1.cancelAndJoin()
                testCoroutineScheduler.advanceTimeBy(5.seconds + 1.milliseconds)

                backgroundScope.launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                    viewModel.contactActivities.collect { }
                }

                Then("refresh is not triggered again") {
                    coVerify(exactly = 1) { contactApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) }
                }
            }
        }
    },
)
