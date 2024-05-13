package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import com.teobaranga.monica.contacts.detail.activities.ContactActivitiesUiState
import com.teobaranga.monica.contacts.detail.activities.ContactActivitiesViewModel
import com.teobaranga.monica.contacts.detail.activities.ContactActivity
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.LocalDate

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
        Crossfade(
            targetState = activities,
            label = "Contact Activities",
        ) { uiState ->
            when (uiState) {
                is ContactActivitiesUiState.Loading -> {
                    ContactActivityPlaceholder()
                }

                is ContactActivitiesUiState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        itemsIndexed(
                            items = uiState.activities,
                            key = { _, item -> item.id },
                            contentType = { _, _ -> "activity" },
                        ) { index, item ->
                            ContactActivity(
                                activity = item,
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            // TODO launch activity view / edit
                                        },
                                    )
                                    .fillMaxWidth()
                                    .padding(all = 24.dp),
                            )
                            if (index < uiState.activities.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactActivity(activity: ContactActivity, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
        Row(
            modifier = Modifier
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val localDateFormatter = LocalDateFormatter.current
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = localDateFormatter.format(activity.date),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ContactActivityPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        repeat(3) {
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 88.dp, height = 24.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.fade(),
                        ),
                )
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .height(96.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.fade(),
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewContactActivity() {
    MonicaTheme {
        ContactActivity(
            activity = ContactActivity(
                id = 1,
                title = "Poker Night",
                description = "It was fun",
                date = LocalDate.now(),
                participants = emptyList(),
            ),
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewContactActivityPlaceholder() {
    MonicaTheme {
        ContactActivityPlaceholder()
    }
}
