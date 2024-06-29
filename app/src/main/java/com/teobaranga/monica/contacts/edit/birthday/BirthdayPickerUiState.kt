package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.contacts.ui.Birthday
import java.time.LocalDate
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@Stable
class BirthdayPickerUiState(initialBirthday: Birthday?) {

    var birthday by mutableStateOf(initialBirthday)

    val ageTextFieldState = TextFieldState("18")

    val monthTextFieldState = TextFieldState(
        MonthDay.now().month.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault(),
        ),
    )
    val dayTextFieldState = TextFieldState(MonthDay.now().dayOfMonth.toString())

    var fullBirthDate by mutableStateOf(LocalDate.now())

    fun setUnknown() {
        birthday = null
    }

    fun setAgeBased() {
        birthday = Birthday.AgeBased(ageTextFieldState.text.toString().toInt())
    }

    fun setUnknownYear() {
        birthday = Birthday.UnknownYear(
            MonthDay.parse("--01-01"),
        )
    }

    fun setFull() {
        birthday = Birthday.Full(OffsetDateTime.of(fullBirthDate, LocalTime.MIN, ZoneOffset.UTC))
    }
}
