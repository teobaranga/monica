package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.preview.PreviewPixel4
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BirthdayPicker(
    birthday: Birthday?,
    onBirthdaySelect: (Birthday?) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    viewModel: BirthdayPickerViewModel = injectedViewModel<BirthdayPickerViewModel, BirthdayPickerViewModelFactory>(
        creationCallback = { factory ->
            factory(birthday)
        },
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
        Text(
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(bottom = 8.dp),
            text = "Birthday",
            style = MaterialTheme.typography.titleMedium,
        )

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
            enabled = !uiState.isError,
        ) {
            Text(text = "Confirm")
        }
    }
}

@Composable
private fun UnknownBirthday(uiState: BirthdayPickerUiState, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val isSelected = uiState.birthday == null
    Row(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = {
                    uiState.setUnknown()
                    focusManager.clearFocus()
                },
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                uiState.setUnknown()
                focusManager.clearFocus()
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
