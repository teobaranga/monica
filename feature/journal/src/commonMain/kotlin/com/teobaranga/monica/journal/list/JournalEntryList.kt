package com.teobaranga.monica.journal.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.teobaranga.monica.account.nav.AccountRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.topappbar.MonicaTopAppBar
import com.teobaranga.monica.core.ui.topappbar.SearchIconButton
import com.teobaranga.monica.journal.list.ui.JournalEntryListScreen
import com.teobaranga.monica.journal.view.JournalEntryRoute
import com.teobaranga.monica.useravatar.UserAvatarIconButton
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun JournalEntryList(
    viewModel: JournalEntryListViewModel = metroViewModel(),
) {
    val navigator = LocalNavigator.current
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    JournalEntryListScreen(
        searchBar = {
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
                                navigator.navigate(AccountRoute)
                            },
                        )
                    }
                },
            )
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
