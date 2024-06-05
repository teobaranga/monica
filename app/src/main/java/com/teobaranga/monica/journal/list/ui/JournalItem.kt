package com.teobaranga.monica.journal.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun JournalItem(journalEntry: JournalEntryListItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = journalEntry.post,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
            val dateFormatter = remember { DateTimeFormatter.ofPattern("MM/dd/yyyy - EEEE") }
            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = dateFormatter.format(journalEntry.date),
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@PreviewPixel4
@Composable
private fun PreviewJournalItem() {
    MonicaTheme {
        JournalItem(
            modifier = Modifier
                .padding(20.dp),
            journalEntry = JournalEntryListItem(
                id = 1,
                title = null,
                post = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                            | incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
                            | exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure
                            | dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                            | Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                            | mollit anim id est laborum.
                            | 
                """.trimMargin(),
                date = OffsetDateTime.now(),
            ),
            onClick = { },
        )
    }
}
