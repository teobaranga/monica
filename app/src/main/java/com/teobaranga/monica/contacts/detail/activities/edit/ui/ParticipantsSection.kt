package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.teobaranga.monica.ui.avatar.UserAvatar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantsSection(uiState: EditContactActivityUiState.Loaded, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "Participants",
            style = MaterialTheme.typography.titleMedium,
        )
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = uiState.participantSearch,
                onValueChange = { search ->
                    uiState.onParticipantSearch(search)
                    expanded = true
                },
                placeholder = {
                    Text(
                        text = "Add a participant by name",
                    )
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                ),
            )
            if (uiState.participantResults.isNotEmpty()) {
                DropdownMenu(
                    modifier = Modifier
                        .exposedDropdownSize(true),
                    properties = PopupProperties(focusable = false),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    uiState.participantResults.forEach { result ->
                        DropdownMenuItem(
                            text = {
                                Text(text = result.name)
                            },
                            onClick = {
                                uiState.onParticipantSearch(TextFieldValue())
                                uiState.participants.add(result)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
        ParticipantFlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 4.dp),
            participants = uiState.participants,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParticipantFlowRow(participants: SnapshotStateList<ActivityParticipant>, modifier: Modifier = Modifier) {
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
private fun ParticipantChip(participant: ActivityParticipant, onRemove: () -> Unit, modifier: Modifier = Modifier) {
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
