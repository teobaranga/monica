package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.edit.ContactEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.text.startVerticalLineShape
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ParticipantsSection(state: ParticipantsState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp),
            text = "Participants",
            style = MaterialTheme.typography.labelLarge,
        )
        ParticipantDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            state = state,
        )
        ParticipantFlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            participants = state.participants,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParticipantDropdownMenu(
    state: ParticipantsState,
    modifier: Modifier = Modifier,
) {
    var shouldExpand by rememberSaveable { mutableStateOf(false) }
    val expanded = shouldExpand && state.suggestions.isNotEmpty()
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            shouldExpand = it
        },
    ) {
        LaunchedEffect(Unit) {
            snapshotFlow { state.participantSearch.text }
                .distinctUntilChanged()
                .collect {
                    if (it.isNotBlank()) {
                        shouldExpand = true
                    }
                }
        }
        val interactionSource = remember { MutableInteractionSource() }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
            state = state.participantSearch,
            interactionSource = interactionSource,
            shape = startVerticalLineShape(interactionSource),
            placeholder = {
                Text(
                    text = "Add a participant by name",
                )
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                shouldExpand = false
            },
        ) {
            state.suggestions.forEach { result ->
                when (result) {
                    is ActivityParticipant.Contact -> {
                        DropdownMenuItem(
                            text = {
                                Text(text = result.name)
                            },
                            onClick = {
                                state.participantSearch.clearText()
                                state.participants.add(result)
                                shouldExpand = false
                            },
                        )
                    }

                    is ActivityParticipant.New -> {
                        val navigator = LocalNavigator.current
                        DropdownMenuItem(
                            text = {
                                Text(text = "Add \"${result.name}\"")
                            },
                            onClick = {
                                // Keep the dropdown open
                                navigator.navigate(ContactEditRoute(contactName = result.name))
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParticipantFlowRow(
    participants: SnapshotStateList<ActivityParticipant.Contact>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val scope = rememberCoroutineScope()
        for (participant in participants) {
            val itemVisibility = remember(participant) { Animatable(1f) }
            ParticipantChip(
                modifier = Modifier
                    .alpha(itemVisibility.value),
                participant = participant,
                onRemove = {
                    scope.launch {
                        itemVisibility.animateTo(targetValue = 0f, animationSpec = tween(220))
                        participants.remove(participant)
                    }
                },
            )
        }
    }
}

@Composable
private fun ParticipantChip(
    participant: ActivityParticipant.Contact,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InputChip(
        modifier = modifier,
        avatar = {
            UserAvatar(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
                userAvatar = participant.avatar,
            )
        },
        label = {
            Text(
                text = participant.name,
            )
        },
        trailingIcon = {
            CloseIcon(
                onClick = onRemove,
            )
        },
        onClick = {
            // Nothing to do
        },
        selected = false,
    )
}

@Composable
private fun CloseIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(4.dp),
        imageVector = Icons.Default.Close,
        contentDescription = "Remove participant",
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewParticipantsSection() {
    MonicaTheme {
        val state = remember { ParticipantsState() }
        ParticipantsSection(
            modifier = Modifier
                .padding(8.dp),
            state = state,
        )
    }
}
