package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.detail.ContactDetail
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.datetime.LocalMonthDayFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme

data class ContactInfoPersonalSection(
    private val birthday: ContactDetail.Birthday?,
) : ContactInfoSection {

    override val title: String = "Personal"

    @Composable
    override fun Content(modifier: Modifier) {
        if (birthday != null) {
            BirthdayItem(birthday = birthday)
        }
    }
}

@Composable
private fun BirthdayItem(birthday: ContactDetail.Birthday) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .offset(y = (-4).dp),
                imageVector = Icons.Default.Cake,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "Birthday",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Text(
            text = when (birthday) {
                is ContactDetail.Birthday.AgeBased -> {
                    "This person is around ${birthday.age} years old"
                }

                is ContactDetail.Birthday.Full -> {
                    val dateFormatter = LocalDateFormatter.current
                    "This person was born on ${birthday.date.format(dateFormatter)} (${birthday.age} years old)"
                }

                is ContactDetail.Birthday.UnknownYear -> {
                    val monthDayFormatter = LocalMonthDayFormatter.current
                    "This person was born on ${birthday.monthDay.format(monthDayFormatter)}"
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPersonalSection() {
    MonicaTheme {
        ContactInfoPersonalSection(
            birthday = ContactDetail.Birthday.AgeBased(29),
        ).Content(modifier = Modifier)
    }
}
