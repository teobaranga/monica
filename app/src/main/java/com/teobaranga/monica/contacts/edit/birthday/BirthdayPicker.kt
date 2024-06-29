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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.detail.ContactDetail.Birthday
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.Zero
import com.teobaranga.monica.ui.button.DateButton
import com.teobaranga.monica.ui.text.MonicaTextField
import com.teobaranga.monica.ui.theme.MonicaTheme
import kotlinx.coroutines.runBlocking
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayPicker(
    birthday: Birthday?,
    onBirthdaySelected: (Birthday?) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val options = listOf(
        null,
        Birthday.AgeBased(18),
        Birthday.UnknownYear(MonthDay.now()),
        Birthday.Full(OffsetDateTime.now(), 24),
    )
    var selectedOption by remember { mutableStateOf(birthday) }
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        windowInsets = WindowInsets.Zero,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(bottom = 8.dp),
            text = "Birthday",
            style = MaterialTheme.typography.titleMedium,
        )
        options.forEach { option ->
            val isSelected = option?.let { it::class } == selectedOption?.let { it::class }
            val radioModifier = Modifier
                .selectable(
                    selected = isSelected,
                    onClick = {
                        selectedOption = option
                    },
                )
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
            val radioButton: @Composable () -> Unit = {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        selectedOption = option
                    },
                )
            }
            when (option) {
                is Birthday.AgeBased -> {
                    val ageBasedBirthday by remember { mutableStateOf(birthday as? Birthday.AgeBased ?: option) }
                    AgeBasedBirthday(
                        modifier = radioModifier
                            .padding(bottom = 12.dp),
                        radioButton = radioButton,
                        ageBasedBirthday = ageBasedBirthday,
                    )
                }

                is Birthday.UnknownYear -> {
                    val unknownYearBirthday by remember { mutableStateOf(birthday as? Birthday.UnknownYear ?: option) }
                    UnknownYearBirthday(
                        modifier = radioModifier
                            .padding(bottom = 12.dp),
                        radioButton = radioButton,
                        unknownYearBirthday = unknownYearBirthday,
                    )
                }

                is Birthday.Full -> {
                    val fullBirthday by remember { mutableStateOf(birthday as? Birthday.Full ?: option) }
                    FullBirthday(
                        modifier = radioModifier,
                        radioButton = radioButton,
                        fullBirthday = fullBirthday,
                    )
                }

                else -> {
                    UnknownBirthday(
                        modifier = radioModifier,
                        radioButton = radioButton,
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 24.dp)
                .navigationBarsPadding(),
            onClick = {
                onBirthdaySelected(selectedOption)
            },
        ) {
            Text(text = "Confirm")
        }
    }
}

@Composable
private fun UnknownBirthday(
    radioButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        radioButton()
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
    ageBasedBirthday: Birthday.AgeBased,
    radioButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            radioButton()
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
                    .widthIn(max = 56.dp),
                state = TextFieldState(ageBasedBirthday.age.toString()),
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
    unknownYearBirthday: Birthday.UnknownYear,
    radioButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            radioButton()
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
                    .widthIn(max = 150.dp),
                state = TextFieldState(
                    unknownYearBirthday.monthDay.month.getDisplayName(
                        TextStyle.FULL,
                        LocalConfiguration.current.locales[0],
                    ),
                ),
            )
            MonicaTextField(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .requiredWidth(56.dp),
                state = TextFieldState(unknownYearBirthday.monthDay.dayOfMonth.toString()),
            )
        }
    }
}

@Composable
private fun FullBirthday(
    fullBirthday: Birthday.Full,
    radioButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            radioButton()
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
            date = fullBirthday.date.toLocalDate(),
            onDateSelected = { },
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
            sheetState = sheetState,
            birthday = null,
            onBirthdaySelected = { },
            onDismissRequest = { },
        )
    }
}
