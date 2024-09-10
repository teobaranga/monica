package com.teobaranga.monica.contacts.detail.bio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme

data class ContactInfoBioSection(
    private val userAvatar: UserAvatar,
    private val fullName: String,
    private val birthday: Birthday?,
    private val gender: String?,
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
                BirthdayItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp),
                    birthday = birthday,
                )
            }
            if (gender != null) {
                GenderItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    gender = gender,
                )
            }
        }
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
            birthday = Birthday.AgeBased(29),
            gender = "Male",
        ).Content(
            modifier = Modifier,
            navigator = EmptyDestinationsNavigator,
        )
    }
}
