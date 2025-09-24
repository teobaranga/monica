package com.teobaranga.monica.journal.view.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.savedstate.serialization.encodeToSavedState
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.ui.CoreRes
import com.teobaranga.monica.core.ui.datetime.DateFormatStyle
import com.teobaranga.monica.core.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.journal.TestJournalComponent
import com.teobaranga.monica.journal.create
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import com.teobaranga.monica.journal.view.JournalEntry
import com.teobaranga.monica.journal.view.JournalEntryRoute
import com.teobaranga.monica.test.PlatformTest
import io.mockk.mockk
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monica.feature.journal.generated.resources.Res
import monica.feature.journal.generated.resources.journal_entry_empty_error
import monica.feature.journal.generated.resources.journal_title_info_mandatory_title_bug
import org.jetbrains.compose.resources.getString
import org.junit.Test
import kotlin.time.Clock
import kotlin.uuid.Uuid

@OptIn(ExperimentalTestApi::class)
class JournalEntryScreenTest : PlatformTest() {

    private val localDateFormatter = LocalDateFormatter(
        locale = Locale.current.platformLocale,
        dateFormatStyle = DateFormatStyle.LONG,
        includeDay = true,
        includeYear = true,
    )

    val component = TestJournalComponent::class.create()
    val savedStateHandle = SavedStateHandle()
    val navigator = mockk<NavHostController>()

    @Test
    fun `Given new entry, When loaded, Then UI is correct`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
            ) {
                JournalEntry(
                    viewModel = component.journalEntryViewModel()(savedStateHandle),
                )
            }
        }

        onNodeWithText("How was your day?")
            .assertIsDisplayed()
        onNodeWithText("Title (optional)")
            .assertIsDisplayed()
        onNodeWithText("Entry")
            .assertIsDisplayed()

        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val formattedButtonDate = localDateFormatter.format(date)
        onNodeWithText(formattedButtonDate)
            .assertIsDisplayed()

        // No overflow menu
        onNodeWithContentDescription("More")
            .assertDoesNotExist()
    }

    @Test
    fun `Given existing entry, When loaded, Then UI is correct`() = runAndroidComposeUiTest<ComponentActivity> {
        component.dao().upsertJournalEntry(
            JournalEntryEntity(
                id = 1,
                uuid = Uuid.random(),
                title = "Title",
                post = "Post",
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    .date,
                created = Clock.System.now(),
                updated = Clock.System.now(),
                syncStatus = SyncStatus.UP_TO_DATE,
            )
        )

        val savedState = encodeToSavedState(JournalEntryRoute(entryId = 1))
        val savedStateHandle = SavedStateHandle.createHandle(savedState, savedState)

        setContent {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
            ) {
                JournalEntry(
                    viewModel = component.journalEntryViewModel()(savedStateHandle),
                )
            }
        }

        onNodeWithText("How was your day?")
            .assertIsDisplayed()
        onNodeWithText("Title")
            .assertIsDisplayed()
        onNodeWithText("Post")
            .assertIsDisplayed()

        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val formattedButtonDate = localDateFormatter.format(date)
        onNodeWithText(formattedButtonDate)
            .assertIsDisplayed()

        // Overflow menu with Delete option
        onNodeWithContentDescription("More")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText("Delete")
            .assertIsDisplayed()
    }

    @Test
    fun `Given blank entry, When saving, Then show error message`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
            ) {
                JournalEntry(
                    viewModel = component.journalEntryViewModel()(savedStateHandle),
                )
            }
        }

        onNodeWithContentDescription("Save journal entry")
            .performClick()

        onNodeWithText(getString(Res.string.journal_entry_empty_error))
            .assertIsDisplayed()
    }

    @Test
    fun `Given title bug info displayed, When dismissing, Then dismisses`() =
        runAndroidComposeUiTest<ComponentActivity> {
            setContent {
                CompositionLocalProvider(
                    LocalNavigator provides navigator,
                ) {
                    JournalEntry(
                        viewModel = component.journalEntryViewModel()(savedStateHandle),
                    )
                }
            }

            onNodeWithText(getString(CoreRes.string.admonition_info_title))
                .assertIsDisplayed()
            onNodeWithText(getString(Res.string.journal_title_info_mandatory_title_bug))
                .assertIsDisplayed()
            onNodeWithText(getString(CoreRes.string.ok_got_it))
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(getString(CoreRes.string.admonition_info_title))
                .assertDoesNotExist()
            onNodeWithText(getString(Res.string.journal_title_info_mandatory_title_bug))
                .assertDoesNotExist()
        }
}
