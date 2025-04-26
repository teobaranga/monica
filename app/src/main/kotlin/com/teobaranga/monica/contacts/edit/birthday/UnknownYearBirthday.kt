package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.ui.text.MonicaTextField
import kotlinx.datetime.Month
import java.time.format.TextStyle

@Composable
internal fun UnknownYearBirthday(uiState: BirthdayPickerUiState, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val isSelected = uiState.birthday is Birthday.UnknownYear
    val onSelectUnknownYear = {
        uiState.setUnknownYear()
    }
    Column(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = {
                    onSelectUnknownYear()
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
                    onSelectUnknownYear()
                    focusManager.clearFocus()
                },
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
            MonthDropdown(
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onSelectUnknownYear()
                        }
                    },
                uiState = uiState.unknownYear,
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
                state = uiState.unknownYear.dayTextFieldState,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                inputTransformation = dayOfMonthInputTransformation(uiState.unknownYear),
                isError = uiState.unknownYear.error != null,
            )
        }
        uiState.unknownYear.error?.let {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonthDropdown(uiState: BirthdayPickerUiState.UnknownYear, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
    ) {
        val locale = LocalConfiguration.current.locales[0]
        val state by derivedStateOf {
            TextFieldState(uiState.month.getDisplayName(TextStyle.FULL, locale))
        }
        MonicaTextField(
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
            state = state,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
            ),
        )
        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize(true)
                .heightIn(max = 260.dp),
            properties = PopupProperties(focusable = false),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            Month.entries.forEach { result ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = result.getDisplayName(TextStyle.FULL, locale),
                        )
                    },
                    onClick = {
                        uiState.month = result
                        expanded = false
                    },
                )
            }
        }
    }
}
