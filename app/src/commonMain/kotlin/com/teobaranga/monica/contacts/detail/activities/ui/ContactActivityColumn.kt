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
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contact.Contact
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.ui.FabHeight
import com.teobaranga.monica.core.ui.FabPadding
import com.teobaranga.monica.core.ui.datetime.DateFormatStyle
import com.teobaranga.monica.core.ui.datetime.rememberLocalizedDateFormatter
import com.teobaranga.monica.core.ui.plus
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.Uuid

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
            key = { _, item -> item.uuid },
            contentType = { _, _ -> "activity" },
        ) { index, item ->
            ContactActivity(
                modifier = Modifier
                    .animateItem()
                    .clickable(
                        onClick = {
                            onActivityClick(item.id)
                        },
                    )
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                activity = item,
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
            val localDateFormatter = rememberLocalizedDateFormatter(dateStyle = DateFormatStyle.MEDIUM)
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

@Preview
@Composable
private fun PreviewContactActivity() {
    MonicaTheme {
        ContactActivity(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            activity = ContactActivity(
                id = 1,
                uuid = Uuid.random(),
                title = "Poker Night",
                description = "It was fun",
                date = LocalSystemClock.current.todayIn(TimeZone.currentSystemDefault()),
                participants = listOf(
                    Contact(
                        id = 1,
                        firstName = "Alice",
                        lastName = null,
                        completeName = "Alice",
                        initials = "A",
                        avatar = UserAvatar(
                            contactId = 1,
                            initials = "A",
                            color = "#FF0000",
                            avatarUrl = null,
                        ),
                        updated = null,
                    ),
                ),
            ),
        )
    }
}
