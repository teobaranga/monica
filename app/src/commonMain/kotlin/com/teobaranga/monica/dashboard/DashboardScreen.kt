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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.account.Account
import com.teobaranga.monica.contact.Contact
import com.teobaranga.monica.contacts.detail.ContactDetailRoute
import com.teobaranga.monica.core.paging.LazyPagingItems
import com.teobaranga.monica.core.paging.collectAsLazyPagingItems
import com.teobaranga.monica.core.paging.itemKey
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.topappbar.MonicaTopAppBar
import com.teobaranga.monica.core.ui.topappbar.SearchIconButton
import com.teobaranga.monica.useravatar.UserAvatar
import com.teobaranga.monica.useravatar.UserAvatarIconButton
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            MonicaTopAppBar(
                modifier = Modifier
                    .statusBarsPadding(),
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
                        .padding(horizontal = 16.dp)
                        .padding(top = 6.dp, bottom = 22.dp),
                    text = "Welcome, ${userUiState?.userInfo?.name ?: "..."}!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                )

                QuickActionsSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                RecentContactsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
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
                .padding(horizontal = 16.dp),
            text = "Recent contacts",
            style = MaterialTheme.typography.titleMedium,
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
                        key = recentContacts.itemKey { contact -> contact.id },
                    ) { index ->
                        val contact = recentContacts[index]
                        if (contact != null) {
                            UserAvatar(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        onContactSelect(contact.id)
                                    },
                                userAvatar = contact.avatar,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
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
                MonicaTopAppBar()
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
