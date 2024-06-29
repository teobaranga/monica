package com.teobaranga.monica.journal.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teobaranga.monica.ui.FabHeight
import com.teobaranga.monica.ui.FabPadding
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
    val pullRefreshState = rememberPullToRefreshState()
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
        }
    }
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            pullRefreshState.endRefresh()
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection),
        topBar = searchBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEntryAdd,
            ) {
                Icon(Icons.Filled.Add, "Add a new entry")
            }
        },
    ) { contentPadding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = WindowInsets.statusBars.asPaddingValues() + PaddingValues(
                    top = SearchBarDefaults.InputFieldHeight + 36.dp,
                    bottom = 20.dp + FabHeight + FabPadding,
                ),
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
            PullToRefreshContainer(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = SearchBarDefaults.InputFieldHeight)
                    .align(Alignment.TopCenter),
                state = pullRefreshState,
            )
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
            searchBar = { },
            lazyItems = journalItems.collectAsLazyPagingItems(),
            isRefreshing = false,
            onRefresh = { },
            onEntryClick = { },
            onEntryAdd = { },
        )
    }
}
