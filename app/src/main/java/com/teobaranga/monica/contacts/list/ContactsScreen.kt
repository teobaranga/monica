package com.teobaranga.monica.contacts.list

import ContactsNavGraph
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.ContactDetailDestination
import com.ramcosta.composedestinations.generated.destinations.ContactEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.ui.MonicaSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf

@Destination<ContactsNavGraph>(start = true)
@Composable
internal fun Contacts(navigator: DestinationsNavigator, viewModel: ContactsViewModel = hiltViewModel()) {
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    ContactsScreen(
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
                    .padding(top = 16.dp),
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
                onSearch = {
                    // TODO: Implement search
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
        onRefresh = viewModel::refresh,
        onContactSelect = { contactId ->
            navigator.navigate(ContactDetailDestination(contactId))
        },
        onContactAdd = {
            navigator.navigate(ContactEditDestination())
        },
    )

    val state by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state == Lifecycle.State.RESUMED) {
            viewModel.onEntriesChanged()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsScreen(
    searchBar: @Composable () -> Unit,
    lazyItems: LazyPagingItems<Contact>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onContactSelect: (Int) -> Unit,
    onContactAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = contentPadding.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = contentPadding + PaddingValues(vertical = 16.dp),
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
                                val contact = lazyItems[it]
                                contact?.id ?: Int.MIN_VALUE
                            },
                        ) {
                            val contact = lazyItems[it]
                            if (contact != null) {
                                ContactItem(
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
            onClick = {
                onContactSelect(contact.id)
            },
        )
        Text(
            modifier = Modifier
                .padding(start = 12.dp),
            text = contact.completeName,
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewContactsScreen() {
    MonicaTheme {
        val lazyItems = flowOf(
            PagingData.from(
                listOf(
                    Contact(
                        id = 1,
                        firstName = "Alice",
                        lastName = null,
                        completeName = "Alice",
                        initials = "A",
                        avatar = UserAvatar(
                            contactId = 1,
                            initials = "A",
                            color = "#FF0000",
                            avatarUrl = null,
                        ),
                        updated = null,
                    ),
                    Contact(
                        id = 2,
                        firstName = "Bob",
                        lastName = null,
                        completeName = "Bob",
                        initials = "B",
                        avatar = UserAvatar(
                            contactId = 2,
                            initials = "B",
                            color = "#00FF00",
                            avatarUrl = null,
                        ),
                        updated = null,
                    ),
                ),
            ),
        )
        ContactsScreen(
            searchBar = {
                MonicaSearchBar(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    userAvatar = { },
                    onSearch = { },
                )
            },
            lazyItems = lazyItems.collectAsLazyPagingItems(),
            isRefreshing = false,
            onRefresh = { },
            onContactSelect = { },
            onContactAdd = { },
        )
    }
}
