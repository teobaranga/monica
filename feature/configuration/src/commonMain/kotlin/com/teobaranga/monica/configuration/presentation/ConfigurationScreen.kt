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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ConfigurationScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConfigurationViewModel = injectedViewModel(),
) {
    ConfigurationScreen(
        modifier = modifier,
        uiState = viewModel.uiState,
        onClose = onClose,
        onResetTips = viewModel::resetTips,
        onRestartApp = viewModel::onRestartApp,
    )
}

@Composable
private fun ConfigurationScreen(
    uiState: ConfigurationUiState,
    onClose: () -> Unit,
    onResetTips: () -> Unit,
    onRestartApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
                Button(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    onClick = onRestartApp,
                    content = {
                        Text("Restart app")
                    },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
            ResetTipsItem(
                onResetTips = {
                    onResetTips()
                    scope.launch {
                        snackbarHostState.showSnackbar("All in-app tips have been reset.")
                    }
                },
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

@Composable
private fun ResetTipsItem(
    onResetTips: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .clickable(onClick = onResetTips),
        headlineContent = {
            Text("Reset all the in-app tips")
        },
        supportingContent = {
            Text("All previously seen tips will be marked as unseen.")
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
            onResetTips = { },
            onRestartApp = { },
        )
    }
}
