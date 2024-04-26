package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.monica.contacts.detail.activities.ContactActivitiesViewModel
import com.teobaranga.monica.contacts.detail.activities.ContactActivity
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.thenIf

data class ContactInfoActivitiesSection(
    private val contactId: Int,
) : ContactInfoSection {

    override val title: String = "Activities"

    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel = hiltViewModel<ContactActivitiesViewModel, ContactActivitiesViewModel.Factory>(
            creationCallback = { factory: ContactActivitiesViewModel.Factory ->
                factory.create(contactId)
            },
        )
        val activities by viewModel.contactActivities.collectAsStateWithLifecycle()
        LazyColumn {
            itemsIndexed(
                items = activities,
                key = { _, item -> item.id },
                contentType = { _, _ -> "activity" },
            ) { index, item ->
                ContactActivity(
                    modifier = Modifier
                        .thenIf(index == 0) {
                            padding(top = 24.dp)
                        }
                        .thenIf(index == activities.size - 1) {
                            padding(bottom = 24.dp)
                        }
                        .padding(horizontal = 24.dp),
                    activity = item,
                )
                if (index < activities.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(vertical = 24.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactActivity(activity: ContactActivity, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = activity.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        if (activity.description != null) {
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewContactActivity() {
    MonicaTheme {
        ContactActivity(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp),
            activity = ContactActivity(
                id = 1,
                title = "Poker Night",
                description = "It was fun",
                date = "Today",
                participants = emptyList(),
            ),
        )
    }
}
