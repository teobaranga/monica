package com.teobaranga.monica.journal.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.core.paging.collectAsLazyPagingItems
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.topappbar.MonicaTopAppBar
import com.teobaranga.monica.core.ui.topappbar.SearchIconButton
import com.teobaranga.monica.journal.list.ui.JournalEntryListScreen
import com.teobaranga.monica.journal.view.JournalEntryRoute
import com.teobaranga.monica.useravatar.UserAvatarIconButton

@Composable
internal fun JournalEntryList(
    viewModel: JournalEntryListViewModel = injectedViewModel(),
) {
    val navigator = LocalNavigator.current
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    JournalEntryListScreen(
        searchBar = {
            var shouldShowAccount by remember { mutableStateOf(false) }
            MonicaTopAppBar(
                actions = {
                    SearchIconButton(
                        onClick = { /* TODO */ },
                    )
                    val userAvatar by viewModel.userAvatar.collectAsStateWithLifecycle()
                    userAvatar?.let {
                        UserAvatarIconButton(
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
        refreshState = refreshState,
        onEntryClick = { id ->
            navigator.navigate(JournalEntryRoute(id))
        },
        onEntryAdd = {
            navigator.navigate(JournalEntryRoute())
        },
    )
}
