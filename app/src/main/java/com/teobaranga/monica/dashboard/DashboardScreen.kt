package com.teobaranga.monica.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.contacts.detail.ContactDetailRoute
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.ui.MonicaSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.navigation.LocalNavigator
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun Dashboard(
    viewModel: DashboardViewModel = injectedViewModel(),
) {
    val navigator = LocalNavigator.current
    val userUiState by viewModel.userUiState.collectAsStateWithLifecycle()
    val recentContacts = viewModel.recentContacts.collectAsLazyPagingItems()
    DashboardScreen(
        searchBar = {
            var shouldShowAccount by remember { mutableStateOf(false) }
            MonicaSearchBar(
                modifier = Modifier
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
        userUiState = userUiState,
        recentContacts = recentContacts,
        onContactSelect = { contactId ->
            navigator.navigate(ContactDetailRoute(contactId))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    searchBar: @Composable () -> Unit,
    userUiState: UserUiState?,
    recentContacts: LazyPagingItems<Contact>,
    onContactSelect: (contactId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = searchBar,
    ) { contentPadding ->
        val visible = remember { MutableTransitionState(false).apply { targetState = true } }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            visibleState = visible,
            enter = EnterTransition.None,
            exit = ExitTransition.None,
        ) {
            Column(
                modifier = Modifier
                    .animateEnterExit(
                        enter = fadeIn() + scaleIn(initialScale = 0.95f),
                        exit = fadeOut() + scaleOut(targetScale = 0.95f),
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(top = 24.dp, bottom = 32.dp),
                    text = "Welcome, ${userUiState?.userInfo?.name ?: "..."}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )

                RecentContactsSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    recentContacts = recentContacts,
                    onContactSelect = onContactSelect,
                )
            }
        }
    }
}

@Composable
private fun RecentContactsSection(
    recentContacts: LazyPagingItems<Contact>,
    onContactSelect: (contactId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = "Recent contacts",
            style = MaterialTheme.typography.titleSmall,
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            when (recentContacts.loadState.refresh) {
                is LoadState.Error -> {
                    // TODO
                }

                is LoadState.Loading,
                is LoadState.NotLoading,
                -> {
                    items(
                        count = recentContacts.itemCount,
                        key = {
                            val contact = recentContacts[it]
                            contact?.id ?: Int.MIN_VALUE
                        },
                    ) { contactId ->
                        val contact = recentContacts[contactId]
                        if (contact != null) {
                            UserAvatar(
                                modifier = Modifier
                                    .size(72.dp),
                                userAvatar = contact.avatar,
                                onClick = {
                                    onContactSelect(contact.id)
                                },
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
private fun PreviewDashboardScreen() {
    MonicaTheme {
        val recentContacts = flowOf(
            PagingData.from(
                listOf(
                    Contact(
                        id = 1,
                        firstName = "Alice",
                        lastName = "B",
                        completeName = "Alice B",
                        initials = "AB",
                        avatar = UserAvatar(
                            contactId = 1,
                            initials = "AB",
                            color = "#709512",
                            avatarUrl = null,
                        ),
                        updated = null,
                    ),
                    Contact(
                        id = 2,
                        firstName = "Charlie",
                        lastName = "D",
                        completeName = "Charlie D",
                        initials = "CD",
                        avatar = UserAvatar(
                            contactId = 2,
                            initials = "CD",
                            color = "#709512",
                            avatarUrl = null,
                        ),
                        updated = null,
                    ),
                ),
            ),
        )
        DashboardScreen(
            searchBar = {
                MonicaSearchBar(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    userAvatar = { },
                    onSearch = { },
                )
            },
            userUiState = UserUiState(
                userInfo = UserUiState.UserInfo(
                    name = "Teo",
                ),
            ),
            recentContacts = recentContacts.collectAsLazyPagingItems(),
            onContactSelect = { },
        )
    }
}
