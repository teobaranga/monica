package com.teobaranga.monica.journal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teobaranga.monica.dashboard.DashboardSearchBar
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun JournalScreen(
    userAvatar: UserAvatar?,
    onAvatarClick: () -> Unit,
    lazyItems: LazyPagingItems<JournalEntry>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val colors = arrayOf(
        0.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
        0.75f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
        1.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.0f),
    )
    if (userAvatar != null) {
        DashboardSearchBar(
            modifier = Modifier
                .background(Brush.verticalGradient(colorStops = colors))
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 20.dp),
            userAvatar = userAvatar,
            onAvatarClick = onAvatarClick,
        )
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = SearchBarDefaults.InputFieldHeight + 20.dp)
            .zIndex(2f)
    ) {
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
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
                                    .padding(horizontal = 20.dp),
                                journalEntry = journalEntry,
                            )
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
                    JournalEntry(
                        id = 1,
                        title = null,
                        post = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                            | incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
                            | exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure
                            | dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                            | Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                            | mollit anim id est laborum.
                            | """.trimMargin(),
                        date = ZonedDateTime.now(),
                        created = ZonedDateTime.now(),
                        updated = ZonedDateTime.now(),
                    ),
                )
            )
        )
        JournalScreen(
            userAvatar = UserAvatar(
                contactId = 1,
                initials = "TB",
                color = "#FF0000",
                avatarUrl = null,
            ),
            onAvatarClick = { /*TODO*/ },
            lazyItems = journalItems.collectAsLazyPagingItems(),
            isRefreshing = false,
            onRefresh = { },
        )
    }
}
