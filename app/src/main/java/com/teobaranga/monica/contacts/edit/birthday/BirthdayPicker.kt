package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.Zero
import com.teobaranga.monica.ui.button.DateButton
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayPicker(
    birthday: Birthday?,
    onBirthdaySelect: (Birthday?) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    viewModel: BirthdayPickerViewModel = hiltViewModel<BirthdayPickerViewModel, BirthdayPickerViewModel.Factory>(
        creationCallback = { factory: BirthdayPickerViewModel.Factory ->
            factory.create(birthday)
        }
    ),
) {
    BirthdayPicker(
        modifier = modifier,
        uiState = viewModel.uiState,
        onBirthdaySelect = onBirthdaySelect,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthdayPicker(
    uiState: BirthdayPickerUiState,
    onBirthdaySelect: (Birthday?) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        contentWindowInsets = { WindowInsets.Zero },
    ) {
        val focusManager = LocalFocusManager.current

        Text(
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(bottom = 8.dp),
            text = "Birthday",
            style = MaterialTheme.typography.titleMedium,
        )

        LaunchedEffect(uiState.birthday) {
            focusManager.clearFocus()
        }
        val radioModifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()

        UnknownBirthday(
            modifier = radioModifier,
            uiState = uiState,
        )

        AgeBasedBirthday(
            modifier = radioModifier
                .padding(bottom = 12.dp),
            uiState = uiState,
        )

        UnknownYearBirthday(
            modifier = radioModifier
                .padding(bottom = 12.dp),
            uiState = uiState,
        )

        FullBirthday(
            modifier = radioModifier,
            uiState = uiState,
        )

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 24.dp)
                .navigationBarsPadding(),
            onClick = {
                onBirthdaySelect(uiState.birthday)
            },
        ) {
            Text(text = "Confirm")
        }
    }
}

@Composable
private fun UnknownBirthday(
    uiState: BirthdayPickerUiState,
    modifier: Modifier = Modifier,
) {
    val isSelected = uiState.birthday == null
    Row(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = {
                    uiState.setUnknown()
                },
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                uiState.setUnknown()
            },
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Unknown",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun AgeBasedBirthday(
    uiState: BirthdayPickerUiState,
    modifier: Modifier = Modifier,
) {
    val isSelected = uiState.birthday is Birthday.AgeBased
    val onSelectAgeBased = {
        uiState.setAgeBased()
    }
    Column(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = {
                    onSelectAgeBased()
                },
            )
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected,
                onClick = {
                    onSelectAgeBased()
                },
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = "This person is probably...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 52.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MonicaTextField(
                modifier = Modifier
                    .widthIn(max = 64.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onSelectAgeBased()
                        }
                    },
                state = uiState.ageTextFieldState,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "years old",
            )
        }
    }
}

@Composable
private fun UnknownYearBirthday(
    uiState: BirthdayPickerUiState,
    modifier: Modifier = Modifier,
) {
    val isSelected = uiState.birthday is Birthday.UnknownYear
    val onSelectUnknownYear = {
        uiState.setUnknownYear()
    }
    Column(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = onSelectUnknownYear,
            )
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelectUnknownYear,
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = "I know the day and the month...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 52.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MonicaTextField(
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onSelectUnknownYear()
                        }
                    },
                state = uiState.monthTextFieldState,
            )
            MonicaTextField(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .requiredWidth(56.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onSelectUnknownYear()
                        }
                    },
                state = uiState.dayTextFieldState,
            )
        }
    }
}

@Composable
private fun FullBirthday(
    uiState: BirthdayPickerUiState,
    modifier: Modifier = Modifier,
) {
    val isSelected = uiState.birthday is Birthday.Full
    val onSelectFull = {
        uiState.setFull()
    }
    Column(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = onSelectFull,
            )
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelectFull,
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = "This person was born on...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        DateButton(
            modifier = Modifier
                .padding(start = 52.dp),
            date = uiState.fullBirthDate,
            onDateSelect = {
                uiState.fullBirthDate = it
                onSelectFull()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewPixel4
@Composable
private fun PreviewBirthdayPicker() {
    MonicaTheme {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        runBlocking { sheetState.show() }
        BirthdayPicker(
            uiState = BirthdayPickerUiState(initialBirthday = null),
            sheetState = sheetState,
            onBirthdaySelect = { },
            onDismissRequest = { },
        )
    }
}
