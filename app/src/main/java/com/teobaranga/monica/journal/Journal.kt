package com.teobaranga.monica.journal

import JournalNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.journal.ui.JournalScreen

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
