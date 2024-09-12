package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.datetime.getMonthDayFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun BirthdaySection(birthday: Birthday?, onBirthdayChange: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = "Birthday",
            style = MaterialTheme.typography.titleMedium,
        )
        TextButton(
            modifier = Modifier
                .padding(top = 4.dp),
            onClick = onBirthdayChange,
        ) {
            Icon(
                modifier = Modifier,
                imageVector = if (birthday == null) {
                    Icons.Outlined.Add
                } else {
                    Icons.Outlined.Celebration
                },
                contentDescription = "Change birthday",
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = when (birthday) {
                    is Birthday.AgeBased -> {
                        "~${birthday.age} years old"
                    }

                    is Birthday.Full -> {
                        val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG) }
                        "${birthday.date.format(dateFormatter)} (${birthday.age} years old)"
                    }

                    is Birthday.UnknownYear -> {
                        val monthDayFormatter = getMonthDayFormatter(dateStyle = FormatStyle.LONG)
                        birthday.monthDay.format(monthDayFormatter)
                    }

                    null -> {
                        "Add birthday"
                    }
                },
            )
        }
    }
}

private class BirthdayPreviewParameterProvider : PreviewParameterProvider<Birthday?> {
    override val values = sequenceOf(
        null,
        Birthday.AgeBased(23),
        Birthday.UnknownYear(MonthDay.now()),
        Birthday.Full(OffsetDateTime.now().minusYears(23L)),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewBirthdaySection(@PreviewParameter(BirthdayPreviewParameterProvider::class) birthday: Birthday?) {
    MonicaTheme {
        BirthdaySection(
            birthday = birthday,
            onBirthdayChange = { },
        )
    }
}
