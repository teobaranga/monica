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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.dashboard.DashboardSearchBar
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.plus

@JournalNavGraph(start = true)
@Destination
@Composable
fun Journal() {
    val viewModel = hiltViewModel<JournalViewModel>()
    val userAvatar by viewModel.userAvatar.collectAsState()
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    JournalScreen(
        userAvatar = userAvatar,
        lazyItems = lazyItems,
        onAvatarClick = {
            // TODO
        },
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
    )
}

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
                is LoadState.NotLoading -> {
                    items(
                        count = lazyItems.itemCount,
                        key = {
                            val journalEntry = lazyItems[it]
                            journalEntry?.id ?: Int.MIN_VALUE
                        },
                    ) {
                        val journalEntry = lazyItems[it]
                        if (journalEntry != null) {
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
            }
        }
    }
}
