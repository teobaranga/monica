package com.teobaranga.monica.contacts.detail.activities.edit.ui

import ContactsNavGraph
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.teobaranga.monica.ui.FabHeight
import com.teobaranga.monica.ui.FabPadding
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EditContactActivity(
        uiState = uiState,
        navigator = navigator,
        title = if (activityId == null) {
            "New activity"
        } else {
            "Edit activity"
        },
        onSave = viewModel::onSave,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditContactActivity(
    uiState: EditContactActivityUiState,
    navigator: DestinationsNavigator,
    title: String,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigator::popBackStack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                onClick = {
                    onSave()
                    navigator.popBackStack()
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
                onAddParticipants = {
                    // TODO
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSection(
    uiState: EditContactActivityUiState,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Date",
            style = MaterialTheme.typography.titleMedium,
        )
        TextButton(
            modifier = Modifier
                .padding(start = 24.dp, top = 12.dp),
            onClick = {
                showDatePickerDialog = true
            },
        ) {
            val dateFormatter = LocalDateFormatter.current
            Icon(
                imageVector = Icons.Outlined.Today,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = dateFormatter.format(uiState.date),
            )
        }
    }
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.date
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
        )
        DatePickerDialog(
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelect(LocalDate.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC))
                        }
                        showDatePickerDialog = false
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            onDismissRequest = {
                showDatePickerDialog = false
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}

@Composable
private fun SummarySection(uiState: EditContactActivityUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParticipantsSection(
    uiState: EditContactActivityUiState,
    onAddParticipants: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Participants",
            style = MaterialTheme.typography.titleMedium,
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            for (participant in uiState.participants) {
                SuggestionChip(
                    icon = {
                        UserAvatar(
                            modifier = Modifier
                                .size(24.dp),
                            userAvatar = participant.avatar,
                            onClick = {
                                // TODO?
                            },
                        )
                    },
                    label = {
                        Text(
                            text = participant.name,
                        )
                    },
                    onClick = {
                        // TODO?
                    },
                )
            }
        }
        TextButton(
            modifier = Modifier
                .padding(start = 24.dp, top = 4.dp),
            onClick = onAddParticipants,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = "Add participants",
            )
        }
    }
}

@Composable
private fun DetailsSection(uiState: EditContactActivityUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEditContactActivityLoadedScreen() {
    MonicaTheme {
        EditContactActivity(
            uiState = EditContactActivityUiState(),
            navigator = EmptyDestinationsNavigator,
            title = "New activity",
            onSave = { },
        )
    }
}
