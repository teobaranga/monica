package com.teobaranga.monica.contacts.list

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.contact.Contact
import com.teobaranga.monica.contacts.detail.ContactDetailRoute
import com.teobaranga.monica.contacts.edit.ContactEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.plus
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshBox
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.topappbar.MonicaTopAppBar
import com.teobaranga.monica.core.ui.topappbar.SearchIconButton
import com.teobaranga.monica.core.ui.util.ScrollToTopEffect
import com.teobaranga.monica.core.ui.util.keepScrollOnSizeChanged
import com.teobaranga.monica.ui.preview.contactAlice
import com.teobaranga.monica.ui.preview.contactBob
import com.teobaranga.monica.useravatar.UserAvatar
import com.teobaranga.monica.useravatar.UserAvatarIconButton
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Contacts(
    viewModel: ContactsViewModel = injectedViewModel(),
) {
    val navigator = LocalNavigator.current
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    ContactsScreen(
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
        uiState = viewModel.state,
        refreshState = refreshState,
        onContactSelect = { contactId ->
            navigator.navigate(ContactDetailRoute(contactId))
        },
        onContactAdd = {
            navigator.navigate(ContactEditRoute())
        },
    )
}

@Composable
private fun ContactsScreen(
    searchBar: @Composable () -> Unit,
    uiState: ContactsUiState,
    refreshState: MonicaPullToRefreshState,
    onContactSelect: (Int) -> Unit,
    onContactAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contacts = uiState.items.collectAsLazyPagingItems()
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = searchBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onContactAdd,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add contact",
                )
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
                    getFirstId = { contacts.itemSnapshotList.items.firstOrNull()?.id },
                )

                LazyColumn(
                    modifier = Modifier
                        .animateEnterExit(
                            enter = fadeIn() + scaleIn(initialScale = 0.95f),
                            exit = fadeOut() + scaleOut(targetScale = 0.95f),
                        ),
                    state = lazyListState,
                    contentPadding = contentPadding + PaddingValues(vertical = 16.dp),
                ) {
                    when (contacts.loadState.refresh) {
                        is LoadState.Error -> {
                            // TODO
                        }

                        is LoadState.Loading,
                        is LoadState.NotLoading,
                        -> {
                            items(
                                count = contacts.itemCount,
                                key = contacts.itemKey { contact ->
                                    contact.id
                                },
                            ) { index ->
                                val contact = contacts[index]
                                if (contact != null) {
                                    ContactItem(
                                        modifier = Modifier
                                            .animateItem(),
                                        contact = contact,
                                        onContactSelect = onContactSelect,
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

@Composable
private fun ContactItem(contact: Contact, onContactSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onContactSelect(contact.id)
            }
            .padding(
                horizontal = 20.dp,
                vertical = 6.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserAvatar(
            modifier = Modifier
                .size(48.dp),
            userAvatar = contact.avatar,
        )
        Text(
            modifier = Modifier
                .padding(start = 12.dp),
            text = contact.completeName,
        )
    }
}

@Preview
@Composable
private fun PreviewContactsScreen() {
    MonicaTheme {
        ContactsScreen(
            searchBar = {
                MonicaTopAppBar()
            },
            uiState = ContactsUiState(
                items = flowOf(PagingData.from(listOf(contactAlice, contactBob))),
            ),
            refreshState = MonicaPullToRefreshState(onRefresh = { }),
            onContactSelect = { },
            onContactAdd = { },
        )
    }
}
