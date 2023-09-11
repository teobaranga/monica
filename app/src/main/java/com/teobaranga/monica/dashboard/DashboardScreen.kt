package com.teobaranga.monica.dashboard

import DashboardNavGraph
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.home.HomeSearchBar

@DashboardNavGraph(start = true)
@Destination
@Composable
fun Dashboard() {
    val viewModel = hiltViewModel<DashboardViewModel>()
    DashboardScreen(
        onClearAuthorization = viewModel::onClearAuthorization,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onClearAuthorization: () -> Unit,
) {
    HomeSearchBar()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = SearchBarDefaults.InputFieldHeight),
    ) {
        Button(
            onClick = onClearAuthorization,
        ) {
            Text(text = "Sign out")
        }
    }
}
