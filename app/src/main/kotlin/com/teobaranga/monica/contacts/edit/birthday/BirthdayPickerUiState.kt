package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.LocalDate

@Stable
class BirthdayPickerUiState(
    initialBirthday: Birthday?,
    nowLocalDate: LocalDate,
    nowMonthDay: MonthDay,
) {

    var birthday by mutableStateOf(initialBirthday)
        private set

    val age = if (initialBirthday is Birthday.AgeBased) {
        Age(initialBirthday.age)
    } else {
        Age()
    }

    val unknownYear = if (initialBirthday is Birthday.UnknownYear) {
        UnknownYear(initialBirthday.monthDay)
    } else {
        UnknownYear(nowMonthDay)
    }

    var fullBirthDate: LocalDate by mutableStateOf(
        if (initialBirthday is Birthday.Full) {
            initialBirthday.date
        } else {
            nowLocalDate
        }
    )

    val isError by derivedStateOf {
        when {
            birthday is Birthday.UnknownYear && unknownYear.error != null -> true
            birthday is Birthday.AgeBased && age.error != null -> true
            else -> false
        }
    }

    fun setUnknown() {
        birthday = null
    }

    fun setAgeBased() {
        age.birthday?.let {
            birthday = it
        }
    }

    fun setUnknownYear() {
        unknownYear.birthday?.let {
            birthday = it
        }
    }

    fun setFull() {
        birthday = Birthday.Full(fullBirthDate)
    }

    @Stable
    class Age(age: Int = 18) {

        val ageTextFieldState = TextFieldState(age.toString())

        val birthday by derivedStateOf {
            try {
                val age = ageTextFieldState.text.toString().toInt()
                Birthday.AgeBased(age)
            } catch (_: Exception) {
                // User entered an invalid age
                null
            }
        }

        var error by mutableStateOf<String?>(null)
    }

    @Stable
    class UnknownYear(monthDay: MonthDay) {

        var month by mutableStateOf(monthDay.month)

        val dayTextFieldState = TextFieldState(monthDay.dayOfMonth.toString())

        val birthday by derivedStateOf {
            try {
                val day = dayTextFieldState.text.toString().toInt()
                Birthday.UnknownYear(MonthDay.of(month, day))
            } catch (_: Exception) {
                // User enter some invalid month/day combination
                null
            }
        }

        var error by mutableStateOf<String?>(null)
    }
}
