package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.ui.FabHeight
import com.teobaranga.monica.ui.FabPadding
import com.teobaranga.monica.ui.datetime.LocalDateFormatter
import com.teobaranga.monica.ui.plus
import com.teobaranga.monica.ui.theme.MonicaTheme
import java.time.LocalDate

@Composable
internal fun ContactActivitiesColumn(
    uiState: ContactActivitiesUiState.Loaded,
    onActivityClick: (activityId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues() +
            PaddingValues(bottom = FabPadding + FabHeight + 24.dp),
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
                            onActivityClick(item.id)
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
        if (activity.participants.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PeopleOutline,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = activity.participants
                        .joinToString(", ") { contact ->
                            contact.completeName
                        },
                    style = MaterialTheme.typography.bodySmall,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            activity = ContactActivity(
                id = 1,
                title = "Poker Night",
                description = "It was fun",
                date = LocalDate.now(),
                participants = listOf(
                    Contact(
                        id = 1,
                        firstName = "Alice",
                        lastName = null,
                        completeName = "Alice",
                        initials = "A",
                        avatarUrl = null,
                        avatarColor = "#FF0000",
                        updated = null,
                    ),
                ),
            ),
        )
    }
}
