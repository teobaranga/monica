package com.teobaranga.monica.journal.view.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.ui.ConfirmExitDialog
import com.teobaranga.monica.core.ui.FabHeight
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.button.DateButton
import com.teobaranga.monica.core.ui.preview.PreviewPixel4
import com.teobaranga.monica.core.ui.rememberConfirmExitDialogState
import com.teobaranga.monica.core.ui.text.MonicaTextField
import com.teobaranga.monica.core.ui.text.MonicaTextFieldDefaults
import com.teobaranga.monica.core.ui.text.startVerticalLineShape
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.util.debounce
import com.teobaranga.monica.core.ui.util.keepCursorVisible
import com.teobaranga.monica.core.ui.util.rememberCursorData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun JournalEntryScreen(
    uiState: JournalEntryUiState,
    topBar: @Composable () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                onClick = debounce(action = onSave),
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save journal entry",
                )
            }
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        Crossfade(
            targetState = uiState,
            label = "Journal Entry Screen",
        ) { uiState ->
            when (uiState) {
                is JournalEntryUiState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth(),
                    )
                }

                is JournalEntryUiState.Loaded -> {
                    val confirmExitDialogState = rememberConfirmExitDialogState().apply {
                        shouldConfirm = uiState.hasChanges
                    }
                    ConfirmExitDialog(
                        state = confirmExitDialogState,
                        title = "Unsaved changes",
                        text = "You have unsaved changes. Are you sure you want to exit?",
                        positiveText = "Keep editing",
                        negativeText = "Exit",
                    )
                    JournalEntryScreenLoaded(
                        modifier = Modifier
                            .padding(contentPadding)
                            .consumeWindowInsets(contentPadding)
                            .imePadding()
                            .navigationBarsPadding()
                            .padding(bottom = FabHeight),
                        uiState = uiState,
                    )
                }
            }
        }
    }
}

@Composable
private fun JournalEntryScreenLoaded(
    uiState: JournalEntryUiState.Loaded,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "How was your day?",
                style = MaterialTheme.typography.titleMedium,
            )
            DateButton(
                date = uiState.date,
                onDateSelect = { date ->
                    uiState.date = date
                },
            )
        }

        TitleTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            uiState = uiState,
        )

        PostTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp),
            uiState = uiState,
        )
    }
}

@Composable
private fun TitleTextField(
    uiState: JournalEntryUiState.Loaded,
    modifier: Modifier = Modifier,
) {
    val titleInteractionSource = remember { MutableInteractionSource() }
    MonicaTextField(
        modifier = modifier,
        interactionSource = titleInteractionSource,
        state = uiState.title,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = "Title (optional)",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        shape = MonicaTextFieldDefaults.startVerticalLineShape(titleInteractionSource),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
        ),
    )
}

@Composable
private fun PostTextField(
    uiState: JournalEntryUiState.Loaded,
    modifier: Modifier = Modifier,
) {
    val postInteractionSource = remember { MutableInteractionSource() }
    val cursorData = rememberCursorData(
        textFieldState = uiState.post,
        cursorVisibilityStrategy = { cursor, boundsInWindow, screenHeight, scrollState ->
            val threshold = screenHeight / 2 + FabHeight.roundToPx()
            boundsInWindow.topLeft.y - scrollState.value + cursor.top - cursor.height > threshold
        },
    )
    MonicaTextField(
        modifier = modifier
            .keepCursorVisible(cursorData),
        interactionSource = postInteractionSource,
        state = uiState.post,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = "Entry",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        shape = MonicaTextFieldDefaults.startVerticalLineShape(postInteractionSource),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
        ),
        onTextLayout = cursorData.textLayoutResult,
        scrollState = cursorData.scrollState,
        contentPadding = OutlinedTextFieldDefaults.contentPadding(
            top = 0.dp,
            bottom = 0.dp,
        ),
    )
}

@PreviewPixel4
@Composable
private fun PreviewJournalEntryScreen() {
    MonicaTheme {
        JournalEntryScreen(
            uiState = JournalEntryUiState.Loaded(
                id = 1,
                initialTitle = null,
                initialPost = "Hello World!",
                initialDate = LocalSystemClock.current.todayIn(TimeZone.currentSystemDefault()),
            ),
            topBar = {
                JournalEntryTopAppBar(
                    onBack = { },
                    onDelete = { },
                )
            },
            onSave = { },
        )
    }
}
