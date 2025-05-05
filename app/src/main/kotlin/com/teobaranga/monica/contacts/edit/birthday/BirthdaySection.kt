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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.core.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

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
                        val dateFormatter = rememberLocalizedDateFormatter()
                        "${dateFormatter.format(birthday.date)} (${birthday.age} years old)"
                    }

                    is Birthday.UnknownYear -> {
                        val monthDayFormatter = rememberLocalizedDateFormatter(includeYear = false)
                        monthDayFormatter.format(birthday.monthDay)
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

    private val localDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    override val values = sequenceOf(
        null,
        Birthday.AgeBased(23),
        Birthday.UnknownYear(MonthDay.from(localDate)),
        Birthday.Full(localDate.minus(23, DateTimeUnit.YEAR)),
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
