package com.teobaranga.monica.dashboard

import DashboardNavGraph
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.ContactDetailDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.ui.MonicaSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf

@Destination<DashboardNavGraph>(start = true)
@Composable
internal fun Dashboard(navigator: DestinationsNavigator, viewModel: DashboardViewModel = hiltViewModel()) {
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
            navigator.navigate(ContactDetailDestination(contactId))
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
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        searchBar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = SearchBarDefaults.InputFieldHeight + 16.dp),
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
        MonicaBackground {
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
                searchBar = { },
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
}
