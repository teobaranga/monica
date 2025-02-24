package com.teobaranga.monica.journal.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.LocalDate
import java.time.format.FormatStyle

@Composable
fun JournalItem(journalEntry: JournalEntryListItem.Entry, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        val dateFormatter = rememberLocalizedDateFormatter(dateStyle = FormatStyle.FULL)
        Text(
            text = dateFormatter.format(journalEntry.date),
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        journalEntry.title?.let { title ->
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = journalEntry.post,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewJournalItem() {
    MonicaTheme {
        Surface {
            JournalItem(
                modifier = Modifier
                    .padding(16.dp),
                journalEntry = JournalEntryListItem.Entry(
                    id = 1,
                    title = "My day",
                    post = """
                    |Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                    |incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
                    |exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure
                    |dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                    |Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                    |mollit anim id est laborum.
                    """.trimMargin().replace('\n', ' '),
                    date = LocalDate.now(),
                ),
            )
        }
    }
}
