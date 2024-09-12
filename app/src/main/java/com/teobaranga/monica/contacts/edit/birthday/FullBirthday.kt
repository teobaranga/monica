package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.button.DateButton

@Composable
internal fun FullBirthday(uiState: BirthdayPickerUiState, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val isSelected = uiState.birthday is Birthday.Full
    val onSelectFull = {
        uiState.setFull()
    }
    Column(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = {
                    onSelectFull()
                    focusManager.clearFocus()
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
                    onSelectFull()
                    focusManager.clearFocus()
                },
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
