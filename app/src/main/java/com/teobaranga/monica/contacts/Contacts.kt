package com.teobaranga.monica.contacts

import ContactsNavGraph
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme

@ContactsNavGraph(start = true)
@Destination
@Composable
fun Contacts() {
    val viewModel = hiltViewModel<ContactsViewModel>()
    when (val uiState = viewModel.uiState) {
        null -> {
            // TODO: shimmer
            Box(modifier = Modifier.fillMaxSize())
        }

        else -> {
            ContactsScreen(
                uiState = uiState,
                onContactSelected = {
                    // TODO
                },
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
fun ContactsScreen(
    uiState: ContactsUiState,
    onContactSelected: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
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
            )
        }
    }
}
