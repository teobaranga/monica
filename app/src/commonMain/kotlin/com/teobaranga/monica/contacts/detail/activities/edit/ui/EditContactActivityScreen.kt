package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.appbar.MonicaBottomAppBar
import com.teobaranga.monica.core.ui.button.DateButton
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.text.startVerticalLineShape
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.core.ui.util.CursorData
import com.teobaranga.monica.core.ui.util.CursorVisibilityStrategy
import com.teobaranga.monica.core.ui.util.debounce
import com.teobaranga.monica.core.ui.util.keepCursorVisible
import com.teobaranga.monica.core.ui.util.rememberCursorData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditContactActivity(
    viewModel: EditContactActivityViewModel =
        injectedViewModel<EditContactActivityViewModel, EditContactActivityViewModel.Factory>(
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
                isEdit = viewModel.activityId != null,
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
    val contentScrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            topAppBar(scrollBehavior)
        },
        bottomBar = {
            MonicaBottomAppBar(
                modifier = Modifier
                    .imePadding(),
                actions = {
                    // Put actions here when available
                },
                floatingActionButton = {
                    SmallFloatingActionButton(
                        onClick = debounce {
                            onSave()
                            onBack()
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save activity",
                        )
                    }
                },
                contentScrollState = contentScrollState,
            )
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        Crossfade(
            targetState = uiState,
            label = "Edit Contact Activity",
        ) { uiState ->
            EditContactActivityContent(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                uiState = uiState,
                contentScrollState = contentScrollState,
            )
        }
    }
}

@Composable
private fun EditContactActivityContent(
    uiState: EditContactActivityUiState,
    contentScrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is EditContactActivityUiState.Loading -> {
            LinearProgressIndicator(
                modifier = modifier,
            )
        }

        is EditContactActivityUiState.Loaded -> {
            val cursorData = rememberCursorData(
                textFieldState = uiState.details,
                cursorVisibilityStrategy = cursorVisibilityStrategy,
                scrollState = contentScrollState,
            )
            EditContactActivityScreenLoaded(
                modifier = modifier,
                uiState = uiState,
                cursorData = cursorData,
            )
        }
    }
}

@Composable
private fun EditContactActivityScreenLoaded(
    uiState: EditContactActivityUiState.Loaded,
    cursorData: CursorData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(cursorData.scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            ParticipantsSection(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 14.dp),
                state = uiState.participantsState,
            )
            DateButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp),
                date = uiState.date,
                onDateSelect = { date ->
                    uiState.date = date
                },
            )
        }

        AnimatedVisibility(
            visible = uiState.participantsState.participants.isNotEmpty(),
        ) {
            ParticipantsRow(
                modifier = Modifier
                    .fillMaxWidth(),
                state = uiState.participantsState,
            )
        }

        SummarySection(
            uiState = uiState,
        )

        DetailsSection(
            textFieldState = uiState.details,
            cursorData = cursorData,
        )

        Spacer(
            modifier = Modifier
                .height(4.dp),
        )
    }
}

@Composable
private fun SummarySection(
    uiState: EditContactActivityUiState.Loaded,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SectionTitle(
            modifier = Modifier
                .padding(start = 32.dp),
            text = "Summary",
        )

        val interactionSource = remember { MutableInteractionSource() }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp),
            interactionSource = interactionSource,
            shape = startVerticalLineShape(interactionSource),
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
        SectionTitle(
            modifier = Modifier
                .padding(start = 32.dp),
            text = "Details",
        )
        val interactionSource = remember { MutableInteractionSource() }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp)
                .keepCursorVisible(cursorData),
            state = textFieldState,
            onTextLayout = cursorData.textLayoutResult,
            interactionSource = interactionSource,
            shape = startVerticalLineShape(interactionSource),
            placeholder = {
                Text(
                    text = "Add more details (optional)",
                )
            },
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 6),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
            ),
        )
    }
}

private val cursorVisibilityStrategy = CursorVisibilityStrategy { cursor, boundsInWindow, screenHeight, _ ->
    boundsInWindow.topLeft.y - (screenHeight - boundsInWindow.bottomRight.y) + cursor.top > screenHeight / 2
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
