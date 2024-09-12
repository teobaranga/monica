package com.teobaranga.monica.journal.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teobaranga.monica.ui.FabPadding
import com.teobaranga.monica.ui.MonicaSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryListScreen(
    searchBar: @Composable () -> Unit,
    lazyItems: LazyPagingItems<JournalEntryListItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEntryClick: (id: Int) -> Unit,
    onEntryAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = searchBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEntryAdd,
            ) {
                Icon(Icons.Filled.Add, "Add a new entry")
            }
        },
    ) { contentPadding ->
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = contentPadding.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = contentPadding + PaddingValues(vertical = 16.dp),
            ) {
                when (lazyItems.loadState.refresh) {
                    is LoadState.Error -> {
                        // TODO
                    }

                    is LoadState.Loading,
                    is LoadState.NotLoading,
                    -> {
                        items(
                            count = lazyItems.itemCount,
                            key = {
                                val journalEntry = lazyItems[it]
                                journalEntry?.id ?: Int.MIN_VALUE
                            },
                        ) {
                            val journalEntry = lazyItems[it]
                            if (journalEntry != null) {
                                JournalItem(
                                    modifier = Modifier
                                        .padding(horizontal = FabPadding),
                                    journalEntry = journalEntry,
                                    onClick = {
                                        onEntryClick(journalEntry.id)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewPixel4
@Composable
private fun PreviewJournalScreen() {
    MonicaTheme {
        val journalItems = flowOf(
            PagingData.from(
                listOf(
                    JournalEntryListItem(
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
                        date = LocalDate.now(),
                    ),
                ),
            ),
        )
        JournalEntryListScreen(
            searchBar = {
                MonicaSearchBar(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    userAvatar = { },
                )
            },
            lazyItems = journalItems.collectAsLazyPagingItems(),
            isRefreshing = false,
            onRefresh = { },
            onEntryClick = { },
            onEntryAdd = { },
        )
    }
}
