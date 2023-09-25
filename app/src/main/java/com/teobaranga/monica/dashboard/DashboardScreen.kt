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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.destinations.AccountDestination
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme

@DashboardNavGraph(start = true)
@Destination
@Composable
fun Dashboard() {
    val viewModel = hiltViewModel<DashboardViewModel>()
    DashboardScreen(
        userUiState = viewModel.userUiState,
        recentContactsUiState = viewModel.recentContactsUiState,
        onAvatarClick = {
            viewModel.navigateTo(AccountDestination)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userUiState: UserUiState?,
    recentContactsUiState: RecentContactsUiState?,
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

        if (recentContactsUiState != null) {
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
                items(recentContactsUiState.contacts) {
                    UserAvatar(
                        modifier = Modifier
                            .size(72.dp),
                        userAvatar = it,
                        onClick = {
                            // TODO: launch contact screen
                        },
                    )
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
                recentContactsUiState = RecentContactsUiState(
                    contacts = listOf(
                        UserAvatar(
                            contactId = 1,
                            initials = "AB",
                            color = "#709512",
                            avatarUrl = null,
                        ),
                        UserAvatar(
                            contactId = 2,
                            initials = "CD",
                            color = "#709512",
                            avatarUrl = null,
                        ),
                    )
                ),
                onAvatarClick = { },
            )
        }
    }
}
