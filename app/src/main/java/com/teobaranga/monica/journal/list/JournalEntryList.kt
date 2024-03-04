package com.teobaranga.monica.journal.list

import JournalNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.destinations.JournalEntryDestination
import com.teobaranga.monica.journal.list.ui.JournalEntryListScreen

@JournalNavGraph(start = true)
@Destination
@Composable
fun JournalEntryList(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<JournalEntryListViewModel>()
    val userAvatar by viewModel.userAvatar.collectAsStateWithLifecycle()
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    JournalEntryListScreen(
        userAvatar = userAvatar,
        lazyItems = lazyItems,
        onAvatarClick = {
            // TODO
        },
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        onEntryClick = { id ->
            navigator.navigate(JournalEntryDestination(id))
        },
        onEntryAdd = {
            navigator.navigate(JournalEntryDestination())
        },
    )
}
