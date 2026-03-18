package com.teobaranga.monica.activity

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.activity.data.ContactActivitiesResponse
import com.teobaranga.monica.core.data.remote.MetaResponse
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.engine.coroutines.backgroundScope
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

// TODO: reuse contact test fixtures along with their IDs
// TODO: migrate from mockk to mokkery and move to commonTest
private const val TEST_CONTACT_ID = 123

class ContactActivitiesViewModelTest : BehaviorSpec(
    {
        isolationMode = IsolationMode.InstancePerRoot
        coroutineTestScope = true

        val component = ActivityComponent::class.create()
        val viewModel = component.activityListViewModel()(TEST_CONTACT_ID)
        val activityApi = component.activityApi()

        Given("activities collected") {

            coEvery { activityApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) } returns ApiResponse.of {
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
                coVerify(exactly = 1) { activityApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) }
            }

            When("off-screen and 5 seconds have passed") {
                job1.cancelAndJoin()
                testCoroutineScheduler.advanceTimeBy(5.seconds + 1.milliseconds)

                backgroundScope.launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                    viewModel.contactActivities.collect { }
                }

                Then("refresh is not triggered again") {
                    coVerify(exactly = 1) { activityApi.getContactActivities(id = TEST_CONTACT_ID, page = 1) }
                }
            }
        }
    },
)
