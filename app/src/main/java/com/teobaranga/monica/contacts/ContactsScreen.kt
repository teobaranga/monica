package com.teobaranga.monica.contacts

import ContactsNavGraph
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.contacts.model.Contact
import com.teobaranga.monica.dashboard.DashboardSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.flowOf

@ContactsNavGraph(start = true)
@Destination
@Composable
fun Contacts() {
    val viewModel = hiltViewModel<ContactsViewModel>()
    val userAvatar by viewModel.userAvatar.collectAsState()
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    ContactsScreen(
        userAvatar = userAvatar,
        onAvatarClick = {
            // TODO
        },
        lazyItems = lazyItems,
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        onContactSelected = {
            // TODO
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ContactsScreen(
    userAvatar: UserAvatar?,
    onAvatarClick: () -> Unit,
    lazyItems: LazyPagingItems<Contact>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onContactSelected: (Int) -> Unit,
) {
    val colors = arrayOf(
        0.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
        0.75f to MaterialTheme.colorScheme.background.copy(alpha = 0.78f),
        1.0f to MaterialTheme.colorScheme.background.copy(alpha = 0.0f),
    )
    if (userAvatar != null) {
        DashboardSearchBar(
            modifier = Modifier
                .background(Brush.verticalGradient(colorStops = colors))
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 20.dp),
            userAvatar = userAvatar,
            onAvatarClick = onAvatarClick,
        )
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = SearchBarDefaults.InputFieldHeight + 20.dp)
            .zIndex(2f)
    ) {
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = WindowInsets.statusBars.asPaddingValues() + PaddingValues(
                top = SearchBarDefaults.InputFieldHeight + 30.dp,
                bottom = 20.dp,
            ),
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
                                onContactSelected = onContactSelected,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onContactSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onContactSelected(contact.id)
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
            userAvatar = contact.userAvatar,
            onClick = {
                onContactSelected(contact.id)
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
        MonicaBackground {
            val lazyItems = flowOf(
                PagingData.from(
                    listOf(
                        Contact(
                            id = 1,
                            firstName = "Alice",
                            lastName = null,
                            completeName = "Alice",
                            initials = "A",
                            avatarUrl = null,
                            avatarColor = "#FF0000",
                            updated = null,
                        ),
                        Contact(
                            id = 2,
                            firstName = "Bob",
                            lastName = null,
                            completeName = "Bob",
                            initials = "B",
                            avatarUrl = null,
                            avatarColor = "#00FF00",
                            updated = null,
                        ),
                    )
                )
            )
            ContactsScreen(
                userAvatar = UserAvatar(
                    contactId = 1,
                    initials = "TB",
                    color = "#FF0000",
                    avatarUrl = null,
                ),
                onAvatarClick = { },
                lazyItems = lazyItems.collectAsLazyPagingItems(),
                isRefreshing = false,
                onRefresh = { },
                onContactSelected = { },
            )
        }
    }
}
