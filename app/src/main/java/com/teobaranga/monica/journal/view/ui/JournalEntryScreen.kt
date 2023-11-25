package com.teobaranga.monica.journal.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.ZonedDateTime

@Composable
fun JournalEntryScreen(
    entry: JournalEntry?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .imePadding(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            value = entry?.title.orEmpty(),
            onValueChange = {
                // TODO
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                Text(
                    text = "Title",
                )
            },
            maxLines = 1,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            value = entry?.post.orEmpty(),
            onValueChange = {
                // TODO
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                Text(
                    text = "How was your day?",
                )
            },
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewJournalEntryScreen() {
    MonicaTheme {
        JournalEntryScreen(
            entry = JournalEntry(
                id = 1,
                title = null,
                post = "Hello World!",
                date = ZonedDateTime.now(),
                created = ZonedDateTime.now(),
                updated = ZonedDateTime.now(),
            ),
        )
    }
}
