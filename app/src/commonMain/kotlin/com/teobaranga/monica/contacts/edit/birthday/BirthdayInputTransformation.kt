package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.byValue
import com.teobaranga.monica.core.datetime.maxLength

internal fun ageInputTransformation(age: BirthdayPickerUiState.Age): InputTransformation {
    return InputTransformation.byValue { _, proposed ->
        val yearsAsString = proposed
            .filter { it.isDigit() }
            .trimStart('0')
            .take(2)

        val years = yearsAsString.toString().toIntOrNull()

        age.error = if (years !in 0..120) {
            "Please enter an age between 0 and 120"
        } else {
            null
        }

        yearsAsString
    }
}

internal fun dayOfMonthInputTransformation(unknownYear: BirthdayPickerUiState.UnknownYear): InputTransformation {
    return InputTransformation.byValue { _, proposed ->
        val month = unknownYear.month
        val dayAsString = proposed
            .filter { it.isDigit() }
            .trimStart('0')
            .take(2)

        val day = dayAsString.toString().toIntOrNull()

        unknownYear.error = if (day !in 1..month.maxLength) {
            "Please enter a day between 1 and ${month.maxLength}"
        } else {
            null
        }

        dayAsString
    }
}
