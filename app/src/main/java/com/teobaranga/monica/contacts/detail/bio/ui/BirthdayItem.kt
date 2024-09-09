package com.teobaranga.monica.contacts.detail.bio.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.datetime.LocalMonthDayFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme

@Composable
internal fun BirthdayItem(birthday: Birthday, modifier: Modifier = Modifier) {
    BioItem(
        modifier = modifier,
        icon = {
            Icon(
                modifier = Modifier
                    .offset(y = -(2).dp),
                imageVector = Icons.Outlined.Cake,
                contentDescription = null,
            )
        },
        title = "Birthday",
        description = when (birthday) {
            is Birthday.AgeBased -> {
                "This person is around ${birthday.age} years old"
            }

            is Birthday.Full -> {
                val dateFormatter = LocalDateFormatter.current
                "This person was born on ${birthday.date.format(dateFormatter)} (${birthday.age} years old)"
            }

            is Birthday.UnknownYear -> {
                val monthDayFormatter = LocalMonthDayFormatter.current
                "This person was born on ${birthday.monthDay.format(monthDayFormatter)}"
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewBirthdayItem() {
    MonicaTheme {
        BirthdayItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            birthday = Birthday.AgeBased(age = 25),
        )
    }
}
