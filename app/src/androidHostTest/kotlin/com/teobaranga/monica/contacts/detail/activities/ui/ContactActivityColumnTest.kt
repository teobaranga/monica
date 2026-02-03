package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runAndroidComposeUiTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.Locale
import kotlin.uuid.Uuid

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class ContactActivityColumnTest {

    @Test
    fun `Date displays correctly`() = runAndroidComposeUiTest<ComponentActivity> {
        RuntimeEnvironment.setQualifiers("en-rUS")
        Locale.setDefault(activity!!.resources.configuration.locales[0])

        val date = LocalDate(2025, 2, 24)

        setContent {
            ContactActivitiesColumn(
                uiState = ContactActivitiesUiState.Loaded(
                    activities = listOf(
                        ContactActivity(
                            id = 1,
                            uuid = Uuid.random(),
                            date = date,
                            title = "Title",
                            description = "Description",
                            participants = emptyList(),
                        ),
                    ),
                ),
                onActivityClick = {},
            )
        }

        onNodeWithText("Feb 24, 2025")
            .assertIsDisplayed()
    }
}
