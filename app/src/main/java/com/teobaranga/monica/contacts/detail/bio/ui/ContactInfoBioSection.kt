package com.teobaranga.monica.contacts.detail.bio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.teobaranga.monica.contacts.detail.ContactDetail
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.datetime.LocalMonthDayFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme

data class ContactInfoBioSection(
    private val userAvatar: UserAvatar,
    private val fullName: String,
    private val birthday: ContactDetail.Birthday?,
) : ContactInfoSection {

    override val title: String = "Bio"

    @Composable
    override fun Content(modifier: Modifier, navigator: DestinationsNavigator) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            UserAvatar(
                modifier = Modifier
                    .padding(top = 68.dp)
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally),
                userAvatar = userAvatar,
                onClick = {
                    // TODO
                },
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 28.dp)
                    .align(Alignment.CenterHorizontally),
                text = fullName,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (birthday != null) {
                BirthdayItem(birthday = birthday)
            }
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
                    .offset(y = (-4).dp)
                    .padding(start = 12.dp),
                imageVector = Icons.Outlined.Cake,
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
            modifier = Modifier
                .padding(top = 2.dp),
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
private fun PreviewBioSection() {
    MonicaTheme {
        ContactInfoBioSection(
            fullName = "John Doe (Johnny)",
            userAvatar = UserAvatar(
                contactId = -1,
                initials = "JD",
                color = "#709512",
                avatarUrl = null,
            ),
            birthday = ContactDetail.Birthday.AgeBased(29),
        ).Content(
            modifier = Modifier,
            navigator = EmptyDestinationsNavigator,
        )
    }
}
