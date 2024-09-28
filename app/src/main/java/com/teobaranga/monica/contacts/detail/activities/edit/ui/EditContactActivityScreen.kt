package com.teobaranga.monica.contacts.detail.activities.edit.ui

import ContactsNavGraph
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.ui.FabHeight
import com.teobaranga.monica.ui.FabPadding
import com.teobaranga.monica.ui.LocalDestinationsNavigator
import com.teobaranga.monica.ui.button.DateButton
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ContactsNavGraph>
@Composable
internal fun EditContactActivity(
    navigator: DestinationsNavigator,
    contactId: Int,
    activityId: Int?,
    viewModel: EditContactActivityViewModel =
        hiltViewModel<EditContactActivityViewModel, EditContactActivityViewModel.Factory>(
            creationCallback = { factory: EditContactActivityViewModel.Factory ->
                factory.create(contactId, activityId)
            },
        ),
) {
    CompositionLocalProvider(
        LocalDestinationsNavigator provides navigator,
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EditContactActivity(
            uiState = uiState,
            topAppBar = { topAppBarScrollBehaviour ->
                EditContactActivityTopAppBar(
                    isEdit = activityId != null,
                    onBack = navigator::popBackStack,
                    onDelete = {
                        viewModel.onDelete()
                        navigator.popBackStack()
                    },
                    scrollBehavior = topAppBarScrollBehaviour,
                )
            },
            onSave = viewModel::onSave,
            onBack = navigator::popBackStack,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditContactActivity(
    uiState: EditContactActivityUiState,
    topAppBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            topAppBar(scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                onClick = {
                    onSave()
                    onBack()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save activity",
                )
            }
        },
        contentWindowInsets = WindowInsets.ime,
    ) { contentPadding ->
        Crossfade(
            targetState = uiState,
            label = "Edit Contact Activity",
        ) { uiState ->
            when (uiState) {
                is EditContactActivityUiState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth(),
                    )
                }
                is EditContactActivityUiState.Loaded -> {
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        DateSection(
                            modifier = Modifier
                                .padding(top = 24.dp),
                            onDateSelect = { date ->
                                uiState.date = date
                            },
                            uiState = uiState,
                        )

                        SummarySection(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            uiState = uiState,
                        )

                        ParticipantsSection(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            uiState = uiState,
                        )

                        DetailsSection(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            uiState = uiState,
                        )

                        Spacer(
                            modifier = Modifier
                                .padding(
                                    WindowInsets.navigationBars.asPaddingValues() +
                                        PaddingValues(bottom = FabPadding + FabHeight + 24.dp),
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSection(
    uiState: EditContactActivityUiState.Loaded,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Date",
            style = MaterialTheme.typography.titleMedium,
        )
        DateButton(
            modifier = Modifier
                .padding(start = 24.dp, top = 12.dp),
            date = uiState.date,
            onDateSelect = onDateSelect,
        )
    }
}

@Composable
private fun SummarySection(uiState: EditContactActivityUiState.Loaded, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .animateContentSize(),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Summary",
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
            value = uiState.summary,
            onValueChange = { summary ->
                uiState.summary = summary
            },
            placeholder = {
                Text(
                    text = "Write a short activity summary",
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
            ),
        )
    }
}

@Composable
private fun DetailsSection(uiState: EditContactActivityUiState.Loaded, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .animateContentSize(),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Details",
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
            value = uiState.details,
            onValueChange = { summary ->
                uiState.details = summary
            },
            placeholder = {
                Text(
                    text = "Add more details (optional)",
                )
            },
            minLines = 3,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreviewEditContactActivityLoadedScreen() {
    MonicaTheme {
        EditContactActivity(
            uiState = EditContactActivityUiState.Loaded(
                onParticipantSearch = { },
                participantResults = MutableStateFlow(emptyList()),
            ),
            topAppBar = { },
            onSave = { },
            onBack = { },
        )
    }
}
