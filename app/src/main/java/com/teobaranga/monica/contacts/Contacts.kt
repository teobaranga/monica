package com.teobaranga.monica.contacts

import ContactsNavGraph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

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
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            items = uiState.contacts,
            key = { it.id },
        ) {
            Text(
                text = it.name,
            )
        }
    }
}
