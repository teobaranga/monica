package com.teobaranga.monica.journal.view.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.Zero
import com.teobaranga.monica.ui.button.DateButton
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.keepCursorVisible
import com.teobaranga.monica.util.compose.rememberCursorData
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
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
                onClick = onSave,
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
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .consumeWindowInsets(contentPadding)
                            .imePadding(),
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
                        val titleInteractionSource = remember { MutableInteractionSource() }
                        val titleIsFocused by titleInteractionSource.collectIsFocusedAsState()
                        MonicaTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
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
                            shape = StartVerticalLineShape { titleIsFocused },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                        )

                        val postInteractionSource = remember { MutableInteractionSource() }
                        val postIsFocused by postInteractionSource.collectIsFocusedAsState()
                        val cursorData = rememberCursorData(uiState.post)
                        MonicaTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(24.dp)
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
                            shape = StartVerticalLineShape { postIsFocused },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                            onTextLayout = cursorData.textLayoutResult,
                            scrollState = cursorData.scrollState,
                        )
                    }
                }
            }
        }
    }
}

@PreviewPixel4
@Composable
private fun PreviewJournalEntryScreen() {
    MonicaTheme {
        JournalEntryScreen(
            uiState = JournalEntryUiState.Loaded(
                id = 1,
                title = null,
                post = "Hello World!",
                date = LocalDate.now(),
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
