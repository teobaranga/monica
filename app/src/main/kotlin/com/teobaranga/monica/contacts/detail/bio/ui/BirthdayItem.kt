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
import com.teobaranga.monica.core.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.core.ui.theme.MonicaTheme

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
                val dateFormatter = rememberLocalizedDateFormatter()
                "This person was born on ${dateFormatter.format(birthday.date)} (${birthday.age} years old)"
            }

            is Birthday.UnknownYear -> {
                val monthDayFormatter = rememberLocalizedDateFormatter(includeYear = false)
                "This person was born on ${monthDayFormatter.format(birthday.monthDay)}"
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
