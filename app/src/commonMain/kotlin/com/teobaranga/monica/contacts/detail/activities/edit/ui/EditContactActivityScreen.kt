package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.ui.FabHeight
import com.teobaranga.monica.core.ui.FabPadding
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.button.DateButton
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.text.MonicaTextField
import com.teobaranga.monica.core.ui.text.MonicaTextFieldDefaults
import com.teobaranga.monica.core.ui.text.startVerticalLineShape
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.util.CursorData
import com.teobaranga.monica.core.ui.util.debounce
import com.teobaranga.monica.core.ui.util.keepCursorVisible
import com.teobaranga.monica.core.ui.util.rememberCursorData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditContactActivity(
    viewModel: EditContactActivityViewModel = injectedViewModel<EditContactActivityViewModel, EditContactActivityViewModel.Factory>(
        creationCallback = { factory ->
            factory(createSavedStateHandle())
        },
    ),
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EditContactActivity(
        uiState = uiState,
        topAppBar = { topAppBarScrollBehaviour ->
            EditContactActivityTopAppBar(
                isEdit = viewModel.contactActivityEditRoute.activityId != null,
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
                onClick = debounce {
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
                    EditContactActivityScreenLoaded(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth(),
                        uiState = uiState,
                    )
                }
            }
        }
    }
}

@Composable
private fun EditContactActivityScreenLoaded(
    uiState: EditContactActivityUiState.Loaded,
    modifier: Modifier = Modifier,
) {
    val cursorData = rememberCursorData(
        textFieldState = uiState.details,
        cursorVisibilityStrategy = { cursor, boundsInWindow, screenHeight, scrollState ->
            boundsInWindow.topLeft.y - (screenHeight - boundsInWindow.bottomRight.y) + cursor.top > screenHeight / 2
        },
    )
    Column(
        modifier = modifier
            .verticalScroll(cursorData.scrollState),
    ) {
        ParticipantsSection(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp),
            state = uiState.participantsState,
        )

        SummarySection(
            modifier = Modifier
                .padding(top = 24.dp),
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
            shape = MonicaTextFieldDefaults.startVerticalLineShape(interactionSource),
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
            shape = MonicaTextFieldDefaults.startVerticalLineShape(interactionSource),
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
@Preview
@Composable
private fun PreviewEditContactActivityLoadedScreen() {
    MonicaTheme {
        EditContactActivity(
            uiState = EditContactActivityUiState.Loaded(
                initialDate = LocalSystemClock.current.todayIn(TimeZone.currentSystemDefault()),
            ),
            topAppBar = { },
            onSave = { },
            onBack = { },
        )
    }
}
