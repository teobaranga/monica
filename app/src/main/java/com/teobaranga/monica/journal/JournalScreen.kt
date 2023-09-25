package com.teobaranga.monica.journal

import JournalNavGraph
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.ui.plus

@JournalNavGraph(start = true)
@Destination
@Composable
fun Journal() {
    val viewModel = hiltViewModel<JournalViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    when (val uiState = uiState) {
        null -> {
            // TODO: shimmer
            Box(modifier = Modifier.fillMaxSize())
        }
        else -> {
            JournalScreen(
                uiState = uiState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    uiState: JournalUiState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = WindowInsets.statusBars.asPaddingValues() + PaddingValues(
            top = SearchBarDefaults.InputFieldHeight + 36.dp,
            bottom = 20.dp,
        ),
    ) {
        items(
            items = uiState.entries,
            key = { it.id },
        ) { journalEntry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    // TODO
                },
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Text(
                        text = journalEntry.post,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
