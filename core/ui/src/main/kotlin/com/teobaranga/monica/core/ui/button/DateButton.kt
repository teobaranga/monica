package com.teobaranga.monica.core.ui.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateButton(date: LocalDate, onDateSelect: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    TextButton(
        modifier = modifier,
        onClick = {
            showDatePickerDialog = true
        },
    ) {
        Icon(
            imageVector = Icons.Default.Today,
            contentDescription = null,
        )
        val formatter = rememberLocalizedDateFormatter()
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = date.format(formatter),
        )
    }
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
        )
        DatePickerDialog(
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelect(LocalDate.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC))
                        }
                        showDatePickerDialog = false
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            onDismissRequest = {
                showDatePickerDialog = false
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDateButton() {
    MonicaTheme {
        DateButton(
            date = LocalDate.now(),
            onDateSelect = { },
        )
    }
}
