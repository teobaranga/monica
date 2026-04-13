package com.teobaranga.monica.activity.edit.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contact.nav.ContactEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.core.ui.text.startVerticalLineShape
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ParticipantsSection(state: ParticipantsState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        SectionTitle(
            modifier = Modifier
                .padding(start = 12.dp),
            text = "Participants",
        )
        ParticipantDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            state = state,
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

@Preview(showBackground = true)
@Composable
private fun PreviewParticipantsSection() {
    MonicaTheme {
        val state = remember {
            ParticipantsState().apply {
                participants.add(
                    ActivityParticipant.Contact(
                        contactId = 1,
                        name = "John",
                        avatar = UserAvatar.default("JD")
                    )
                )
                participants.add(
                    ActivityParticipant.Contact(
                        contactId = 2,
                        name = "Jane",
                        avatar = UserAvatar.default("JD")
                    )
                )
            }
        }
        ParticipantsSection(
            modifier = Modifier
                .padding(8.dp),
            state = state,
        )
    }
}
