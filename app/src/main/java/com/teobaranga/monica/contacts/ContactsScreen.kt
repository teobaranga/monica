package com.teobaranga.monica.contacts

import ContactsNavGraph
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.dashboard.DashboardSearchBar
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme

@ContactsNavGraph(start = true)
@Destination
@Composable
fun Contacts() {
    val viewModel = hiltViewModel<ContactsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val userAvatar by viewModel.userAvatar.collectAsState()
    when (userAvatar) {
        null -> {
            // TODO: shimmer
            Box(modifier = Modifier.fillMaxSize())
        }
        else -> {
            ContactsScreen(
                userAvatar = userAvatar,
                uiState = uiState,
                onContactSelected = {
                    // TODO
                },
                onAvatarClick = {
                    // TODO
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    userAvatar: UserAvatar?,
    uiState: ContactsUiState,
    onAvatarClick: () -> Unit,
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = WindowInsets.statusBars.asPaddingValues() + PaddingValues(
            top = SearchBarDefaults.InputFieldHeight + 36.dp,
            bottom = 20.dp,
        ),
    ) {
        items(
            items = uiState.contacts,
            key = { it.id },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onContactSelected(it.id)
                    }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(
                    modifier = Modifier
                        .size(48.dp),
                    userAvatar = it.userAvatar,
                    onClick = {
                        onContactSelected(it.id)
                    },
                )
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = it.name,
                )
            }
        }
    }
}

@PreviewPixel4
@Composable
private fun PreviewContactsScreen() {
    MonicaTheme {
        MonicaBackground {
            ContactsScreen(
                userAvatar = UserAvatar(
                    contactId = 1,
                    initials = "TB",
                    color = "#FF0000",
                    avatarUrl = null,
                ),
                uiState = ContactsUiState(
                    contacts = listOf(
                        ContactsUiState.Contact(
                            id = 1,
                            name = "Alice",
                            userAvatar = UserAvatar(
                                contactId = 1,
                                initials = "A",
                                color = "#FF0000",
                                avatarUrl = null,
                            ),
                        ),
                        ContactsUiState.Contact(
                            id = 2,
                            name = "Bob",
                            userAvatar = UserAvatar(
                                contactId = 2,
                                initials = "B",
                                color = "#00FF00",
                                avatarUrl = null,
                            ),
                        ),
                    )
                ),
                onContactSelected = { },
                onAvatarClick = { },
            )
        }
    }
}
