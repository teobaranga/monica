package com.teobaranga.monica.journal.view.ui

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.OffsetDateTime

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryScreen(entry: JournalEntry?, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                title = {
                    Text("Journal Entry")
                },
                actions = {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Icon(Icons.Filled.Check, "Add a new entry")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding(),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
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
                shape = StartVerticalLineShape,
            )
            val scrollState = rememberScrollState()
            val keyb by keyboardAsState()
            LaunchedEffect(keyb) {
                if (keyb) {
                    scrollState.scrollBy(20f)
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
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
                shape = StartVerticalLineShape,
            )
        }
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
                date = OffsetDateTime.now(),
                created = OffsetDateTime.now(),
                updated = OffsetDateTime.now(),
            ),
        )
    }
}
