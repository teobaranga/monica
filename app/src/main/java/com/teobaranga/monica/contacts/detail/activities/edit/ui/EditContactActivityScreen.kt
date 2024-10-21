package com.teobaranga.monica.contacts.detail.activities.edit.ui

import ContactsNavGraph
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.teobaranga.monica.journal.view.ui.StartVerticalLineShape
import com.teobaranga.monica.ui.FabHeight
import com.teobaranga.monica.ui.FabPadding
import com.teobaranga.monica.ui.Zero
import com.teobaranga.monica.ui.button.DateButton
import com.teobaranga.monica.ui.navigation.LocalDestinationsNavigator
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.CursorData
import com.teobaranga.monica.util.compose.CursorVisibilityStrategy
import com.teobaranga.monica.util.compose.keepCursorVisible
import com.teobaranga.monica.util.compose.rememberCursorData
import kotlinx.coroutines.flow.MutableStateFlow

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
        val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EditContactActivity(
            uiState = uiState,
            topAppBar = { topAppBarScrollBehaviour ->
                EditContactActivityTopAppBar(
                    isEdit = activityId != null,
                    onBack = {
                        onBackPressedDispatcher?.onBackPressed()
                    },
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
                    .navigationBarsPadding()
                    .imePadding(),
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
        contentWindowInsets = WindowInsets.Zero,
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
                    val cursorData = rememberCursorData(
                        textFieldState = uiState.details,
                        cursorVisibilityStrategy = CursorVisibilityStrategy { cursor, boundsInWindow, screenHeight, scrollState ->
                            boundsInWindow.topLeft.y - (screenHeight - boundsInWindow.bottomRight.y)  + cursor.top > screenHeight / 2
                        },
                    )
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth()
                            .verticalScroll(cursorData.scrollState),
                    ) {
                        SummarySection(
                            modifier = Modifier
                                .padding(top = 24.dp),
                            uiState = uiState,
                        )

                        ParticipantsSection(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            uiState = uiState,
                        )

                        DetailsSection(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .navigationBarsPadding()
                                .imePadding()
                                .padding(bottom = FabPadding + FabHeight),
                            textFieldState = uiState.details,
                            cursorData = cursorData,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummarySection(uiState: EditContactActivityUiState.Loaded, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .weight(1f),
                text = "Summary",
                style = MaterialTheme.typography.titleMedium,
            )
            DateButton(
                modifier = Modifier
                    .padding(end = 24.dp),
                date = uiState.date,
                onDateSelect = { date ->
                    uiState.date = date
                },
            )
        }

        val interactionSource = remember { MutableInteractionSource() }
        MonicaTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
            interactionSource = interactionSource,
            shape = StartVerticalLineShape(interactionSource),
            state = uiState.summary,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsSection(
    textFieldState: TextFieldState,
    cursorData: CursorData,
    modifier: Modifier = Modifier,
) {
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
        val interactionSource = remember { MutableInteractionSource() }
        MonicaTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp)
                .keepCursorVisible(cursorData),
            state = textFieldState,
            onTextLayout = cursorData.textLayoutResult,
            interactionSource = interactionSource,
            shape = StartVerticalLineShape(interactionSource),
            placeholder = {
                Text(
                    text = "Add more details (optional)",
                )
            },
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3),
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
                participantResults = MutableStateFlow(emptyList()),
            ),
            topAppBar = { },
            onSave = { },
            onBack = { },
        )
    }
}
