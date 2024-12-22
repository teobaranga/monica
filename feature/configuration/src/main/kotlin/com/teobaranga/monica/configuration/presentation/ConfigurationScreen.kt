package com.teobaranga.monica.configuration.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.inject.runtime.injectedViewModel
import com.teobaranga.monica.ui.theme.MonicaTheme

@Composable
internal fun ConfigurationScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConfigurationViewModel = injectedViewModel(),
) {
    ConfigurationScreen(
        uiState = viewModel.uiState,
        onClose = onClose,
        modifier = modifier,
    )
}

@Composable
private fun ConfigurationScreen(
    uiState: ConfigurationUiState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ConfigurationTopAppBar(
                onClose = onClose,
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentAlignment = Center,
            ) {
                val context = LocalContext.current
                Button(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    onClick = {
                        restartApp(context)
                    },
                    content = {
                        Text("Restart app")
                    },
                )
            }
        },
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxWidth(),
        ) {
            ClearDatabaseItem(
                uiState = uiState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigurationTopAppBar(onClose: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text("Configuration")
        },
        navigationIcon = {
            IconButton(
                onClick = onClose,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
    )
}

@Composable
private fun ClearDatabaseItem(uiState: ConfigurationUiState, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier
            .clickable(
                onClick = {
                    uiState.shouldClearDatabaseOnNextLaunch = !uiState.shouldClearDatabaseOnNextLaunch
                },
            ),
        headlineContent = {
            Text("Clear database on next launch")
        },
        supportingContent = {
            Text("Warning: any unsynchronized changes will be lost.")
        },
        trailingContent = {
            Checkbox(
                checked = uiState.shouldClearDatabaseOnNextLaunch,
                onCheckedChange = {
                    uiState.shouldClearDatabaseOnNextLaunch = it
                },
            )
        },
    )
}

@Preview
@Composable
private fun PreviewConfigurationScreen() {
    MonicaTheme {
        ConfigurationScreen(
            uiState = ConfigurationUiState(),
            onClose = { },
        )
    }
}
