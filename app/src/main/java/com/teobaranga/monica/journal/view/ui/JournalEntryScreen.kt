package com.teobaranga.monica.journal.view.ui

import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                                uiState = uiState,
                            )
                        }
                        MonicaTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            state = uiState.title,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = {
                                Text(
                                    text = "Title (optional)",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            shape = StartVerticalLineShape,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                        )

                        MonicaTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(24.dp),
                            state = uiState.post,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = {
                                Text(
                                    text = "Entry",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            shape = StartVerticalLineShape,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateButton(uiState: JournalEntryUiState.Loaded, modifier: Modifier = Modifier) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    TextButton(
        modifier = modifier,
        onClick = {
            showDatePickerDialog = true
        },
    ) {
        val formatter = LocalDateFormatter.current
        Text(
            text = uiState.date.format(formatter),
        )
    }
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.date
                .toInstant()
                .toEpochMilli(),
        )
        DatePickerDialog(
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            uiState.date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                        }
                        showDatePickerDialog = false
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            onDismissRequest = {
                showDatePickerDialog = false
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
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
                date = OffsetDateTime.now(),
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
