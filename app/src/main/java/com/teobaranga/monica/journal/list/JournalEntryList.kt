package com.teobaranga.monica.journal.list

import JournalNavGraph
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.JournalEntryDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.journal.list.ui.JournalEntryListScreen
import com.teobaranga.monica.ui.MonicaSearchBar
import com.teobaranga.monica.ui.avatar.UserAvatar

@Destination<JournalNavGraph>(start = true)
@Composable
internal fun JournalEntryList(
    navigator: DestinationsNavigator,
    viewModel: JournalEntryListViewModel = hiltViewModel(),
) {
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    JournalEntryListScreen(
        searchBar = {
            var shouldShowAccount by remember { mutableStateOf(false) }
            val colors = arrayOf(
                0.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
                0.75f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
                1.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.0f),
            )
            MonicaSearchBar(
                modifier = Modifier
                    .background(Brush.verticalGradient(colorStops = colors))
                    .statusBarsPadding()
                    .padding(top = 16.dp, bottom = 20.dp),
                userAvatar = {
                    val userAvatar by viewModel.userAvatar.collectAsStateWithLifecycle()
                    userAvatar?.let {
                        UserAvatar(
                            userAvatar = it,
                            onClick = {
                                shouldShowAccount = true
                            },
                        )
                    }
                },
            )
            if (shouldShowAccount) {
                Account(
                    onDismissRequest = {
                        shouldShowAccount = false
                    },
                )
            }
        },
        lazyItems = lazyItems,
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
