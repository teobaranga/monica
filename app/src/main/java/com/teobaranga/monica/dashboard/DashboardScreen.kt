package com.teobaranga.monica.dashboard

import DashboardNavGraph
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.contacts.list.userAvatar
import com.teobaranga.monica.destinations.AccountDestination
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf

@DashboardNavGraph(start = true)
@Destination
@Composable
fun Dashboard() {
    val viewModel = hiltViewModel<DashboardViewModel>()
    val userUiState by viewModel.userUiState.collectAsStateWithLifecycle()
    val recentContacts = viewModel.recentContacts.collectAsLazyPagingItems()
    DashboardScreen(
        userUiState = userUiState,
        recentContacts = recentContacts,
        onAvatarClick = {
            viewModel.navigateTo(AccountDestination)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userUiState: UserUiState?,
    recentContacts: LazyPagingItems<Contact>,
    onAvatarClick: () -> Unit,
) {
    if (userUiState != null) {
        DashboardSearchBar(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 16.dp),
            userAvatar = userUiState.avatar,
            onAvatarClick = onAvatarClick,
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = SearchBarDefaults.InputFieldHeight + 16.dp),
    ) {
        if (userUiState != null) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(top = 24.dp, bottom = 32.dp),
                text = "Welcome, ${userUiState.userInfo.name}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }

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
            contentPadding = PaddingValues(horizontal = 20.dp)
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
                    ) {
                        val contact = recentContacts[it]
                        if (contact != null) {
                            UserAvatar(
                                modifier = Modifier
                                    .size(72.dp),
                                userAvatar = contact.userAvatar,
                                onClick = {
                                    // TODO: launch contact screen
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
                            avatarUrl = null,
                            avatarColor = "#709512",
                            updated = null,
                        ),
                        Contact(
                            id = 2,
                            firstName = "Charlie",
                            lastName = "D",
                            completeName = "Charlie D",
                            initials = "CD",
                            avatarUrl = null,
                            avatarColor = "#709512",
                            updated = null,
                        ),
                    )
                )
            )
            DashboardScreen(
                userUiState = UserUiState(
                    userInfo = UserUiState.UserInfo(
                        name = "Teo",
                    ),
                    avatar = UserAvatar(
                        contactId = 0,
                        initials = "TB",
                        color = "#709512",
                        avatarUrl = null,
                    ),
                ),
                recentContacts = recentContacts.collectAsLazyPagingItems(),
                onAvatarClick = { },
            )
        }
    }
}
