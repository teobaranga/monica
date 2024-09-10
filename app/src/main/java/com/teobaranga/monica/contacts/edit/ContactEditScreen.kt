package com.teobaranga.monica.contacts.edit

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.edit.birthday.BirthdayPicker
import com.teobaranga.monica.contacts.edit.birthday.BirthdaySection
import com.teobaranga.monica.contacts.edit.gender.GenderSection
import com.teobaranga.monica.contacts.edit.ui.ContactEditTopAppBar
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.Zero
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactEditScreen(
    uiState: ContactEditUiState,
    topBar: @Composable () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                onClick = onSave,
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save contact",
                )
            }
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        Crossfade(
            targetState = uiState,
            label = "Contact Edit Screen",
        ) { uiState ->
            when (uiState) {
                is ContactEditUiState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxWidth(),
                    )
                }

                is ContactEditUiState.Loaded -> {
                    var shouldShowBirthdayPicker by rememberSaveable { mutableStateOf(false) }
                    ContactEditLoaded(
                        modifier = Modifier
                            .padding(contentPadding)
                            .consumeWindowInsets(contentPadding)
                            .imePadding(),
                        uiState = uiState,
                        onBirthdayChange = {
                            shouldShowBirthdayPicker = true
                        },
                    )
                    if (shouldShowBirthdayPicker) {
                        BirthdayPicker(
                            onDismissRequest = {
                                shouldShowBirthdayPicker = false
                            },
                            birthday = uiState.birthday,
                            onBirthdaySelect = { birthday ->
                                uiState.birthday = birthday
                                shouldShowBirthdayPicker = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactEditLoaded(
    uiState: ContactEditUiState.Loaded,
    onBirthdayChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        NameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            state = uiState.firstName,
            placeholder = "First name",
        )
        NameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            state = uiState.lastName,
            placeholder = "Last name (optional)",
        )
        NameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            state = uiState.nickname,
            placeholder = "Nickname (optional)",
        )

        GenderSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            gender = uiState.gender,
            onGenderChange = { gender ->
                uiState.gender = gender
            },
            genders = uiState.genders,
        )

        BirthdaySection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            birthday = uiState.birthday,
            onBirthdayChange = onBirthdayChange,
        )
    }
}

@Composable
private fun NameTextField(state: TextFieldState, placeholder: String, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    MonicaTextField(
        modifier = modifier,
        state = state,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = if (!isFocused) {
            {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            null
        },
        label = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
        ),
        interactionSource = interactionSource,
    )
}

@PreviewPixel4
@Composable
private fun PreviewContactEditScreen() {
    MonicaTheme {
        ContactEditScreen(
            uiState = ContactEditUiState.Loaded(
                id = 1,
                firstName = "",
                lastName = null,
                nickname = null,
                initialGender = null,
                genders = emptyList(),
                initialBirthday = null,
            ),
            topBar = {
                ContactEditTopAppBar(
                    onBack = { },
                    onDelete = { },
                )
            },
            onSave = { },
        )
    }
}
