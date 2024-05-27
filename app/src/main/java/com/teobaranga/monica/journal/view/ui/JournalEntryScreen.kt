package com.teobaranga.monica.journal.view.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.journal.model.JournalEntryUiState
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.OffsetDateTime

@Composable
fun JournalEntryScreen(entry: JournalEntryUiState, topBar: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding(),
        ) {
            MonicaTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = entry.title,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "Title",
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
                    .padding(16.dp),
                state = entry.post,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "How was your day?",
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

@PreviewPixel4
@Composable
private fun PreviewJournalEntryScreen() {
    MonicaTheme {
        JournalEntryScreen(
            entry = JournalEntryUiState(
                id = 1,
                title = null,
                post = "Hello World!",
                date = OffsetDateTime.now(),
                created = OffsetDateTime.now(),
                updated = OffsetDateTime.now(),
            ),
            topBar = {
                JournalEntryTopAppBar(
                    onBack = { },
                    onSave = { },
                    onDelete = { },
                )
            },
        )
    }
}
