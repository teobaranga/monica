package com.teobaranga.monica.dashboard

import DashboardNavGraph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.teobaranga.monica.ui.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme

@DashboardNavGraph(start = true)
@Destination
@Composable
fun Dashboard() {
    val viewModel = hiltViewModel<DashboardViewModel>()
    when (val uiState = viewModel.uiState) {
        null -> {
            // TODO: shimmer
            Box(modifier = Modifier.fillMaxSize())
        }

        else -> {
            DashboardScreen(
                uiState = uiState,
                onAvatarClick = {
                    viewModel.navigateTo(AccountDestination)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onAvatarClick: () -> Unit,
) {
    DashboardSearchBar(
        userAvatar = uiState.avatar,
        onAvatarClick = onAvatarClick,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SearchBarDefaults.InputFieldHeight),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 24.dp, bottom = 32.dp),
            text = "Welcome, ${uiState.userInfo.name}",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewPixel4
@Composable
private fun PreviewDashboardScreen() {
    MonicaTheme {
        MonicaBackground {
            DashboardScreen(
                uiState = DashboardUiState(
                    userInfo = DashboardUiState.UserInfo(
                        name = "Teo",
                    ),
                    avatar = UserAvatar(
                        initials = "TB",
                        color = "#709512",
                        data = null,
                    ),
                ),
                onAvatarClick = { },
            )
        }
    }
}
