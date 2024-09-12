package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.text.MonicaTextField

@Composable
internal fun AgeBasedBirthday(uiState: BirthdayPickerUiState, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
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
                    onSelectAgeBased()
                    focusManager.clearFocus()
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
                state = uiState.age.ageTextFieldState,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                inputTransformation = ageInputTransformation(uiState.age),
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = "years old",
            )
        }
    }
}
