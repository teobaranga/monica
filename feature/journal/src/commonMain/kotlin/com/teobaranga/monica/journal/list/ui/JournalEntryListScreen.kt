package com.teobaranga.monica.journal.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.paging.LazyPagingItems
import com.teobaranga.monica.core.paging.collectAsLazyPagingItems
import com.teobaranga.monica.core.paging.itemContentType
import com.teobaranga.monica.core.paging.itemKey
import com.teobaranga.monica.core.ui.datetime.DateFormatStyle
import com.teobaranga.monica.core.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.core.ui.plus
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshBox
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.topappbar.MonicaTopAppBar
import com.teobaranga.monica.core.ui.util.ScrollToTopEffect
import com.teobaranga.monica.core.ui.util.keepScrollOnSizeChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun JournalEntryListScreen(
    searchBar: @Composable () -> Unit,
    lazyItems: LazyPagingItems<JournalEntryListItem>,
    refreshState: MonicaPullToRefreshState,
    onEntryClick: (id: Int) -> Unit,
    onEntryAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = searchBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEntryAdd,
            ) {
                Icon(Icons.Filled.Add, "Add a new entry")
            }
        },
    ) { contentPadding ->
        MonicaPullToRefreshBox(
            state = refreshState,
            indicator = {
                MonicaIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = contentPadding.calculateTopPadding()),
                )
            },
        ) {
            val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }
            val lazyListState = rememberLazyListState()
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize()
                    .keepScrollOnSizeChanged(lazyListState),
                visibleState = visibleState,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                ScrollToTopEffect(
                    lazyListState = lazyListState,
                    getFirstId = {
                        lazyItems.itemSnapshotList.items
                            .filterIsInstance<JournalEntryListItem.Entry>()
                            .firstOrNull()?.id
                    },
                )

                LazyColumn(
                    modifier = Modifier
                        .animateEnterExit(
                            enter = fadeIn() + scaleIn(initialScale = 0.95f),
                            exit = fadeOut() + scaleOut(targetScale = 0.95f),
                        ),
                    state = lazyListState,
                    contentPadding = contentPadding
                        + PaddingValues(vertical = 16.dp)
                        + PaddingValues(bottom = 72.dp),
                ) {
                    when (lazyItems.loadState.refresh) {
                        is LoadState.Error -> {
                            // TODO
                        }

                        is LoadState.Loading,
                        is LoadState.NotLoading,
                        -> {
                            journalEntryListItems(
                                lazyItems = lazyItems,
                                onEntryClick = onEntryClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.journalEntryListItems(
    lazyItems: LazyPagingItems<JournalEntryListItem>,
    onEntryClick: (id: Int) -> Unit,
) {
    items(
        count = lazyItems.itemCount,
        key = lazyItems.itemKey { journalEntry ->
            when (journalEntry) {
                is JournalEntryListItem.Entry -> journalEntry.id
                is JournalEntryListItem.SectionTitle -> buildString {
                    if (journalEntry.year != null) {
                        append(journalEntry.year)
                        append(" ")
                    }
                    append(journalEntry.month)
                }

                is JournalEntryListItem.Divider -> "divider_${Random.nextInt()}"
            }
        },
        contentType = lazyItems.itemContentType { journalEntryItem ->
            when (journalEntryItem) {
                is JournalEntryListItem.Entry -> "entry"
                is JournalEntryListItem.SectionTitle -> "section_title"
                is JournalEntryListItem.Divider -> "divider"
            }
        },
    ) { index ->
        val journalEntry = lazyItems[index]
        JournalEntryListItem(
            journalEntry = journalEntry,
            onEntryClick = onEntryClick,
        )
    }
}

@Composable
private fun LazyItemScope.JournalEntryListItem(
    journalEntry: JournalEntryListItem?,
    onEntryClick: (id: Int) -> Unit,
) {
    when (journalEntry) {
        is JournalEntryListItem.Entry -> {
            JournalItem(
                modifier = Modifier
                    .clickable {
                        onEntryClick(journalEntry.id)
                    }
                    .padding(horizontal = 28.dp)
                    .animateItem(),
                journalEntry = journalEntry,
            )
        }

        is JournalEntryListItem.SectionTitle -> {
            val formatter = rememberLocalizedDateFormatter(dateStyle = DateFormatStyle.FULL, includeDay = false)
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 26.dp),
                text = when {
                    journalEntry.year != null -> formatter.format(
                        YearMonth(journalEntry.year, journalEntry.month)
                    )

                    else -> formatter.format(journalEntry.month)
                },
                style = MaterialTheme.typography.titleLarge,
            )
        }

        is JournalEntryListItem.Divider -> {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }

        null -> {
            // Nothing to render
        }
    }
}

@Preview
@Composable
private fun PreviewJournalScreen() {
    MonicaTheme {
        val journalItems = flowOf(
            PagingData.from(
                listOf<JournalEntryListItem>(
                    JournalEntryListItem.Entry(
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
                        date = LocalSystemClock.current.todayIn(TimeZone.currentSystemDefault()),
                    ),
                ),
            ),
        )
        JournalEntryListScreen(
            searchBar = {
                MonicaTopAppBar()
            },
            lazyItems = journalItems.collectAsLazyPagingItems(),
            refreshState = MonicaPullToRefreshState(onRefresh = { }),
            onEntryClick = { },
            onEntryAdd = { },
        )
    }
}
