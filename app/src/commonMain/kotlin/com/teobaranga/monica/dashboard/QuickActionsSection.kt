package com.teobaranga.monica.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.detail.activities.edit.ui.ContactActivityEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.journal.view.JournalEntryRoute

@Composable
internal fun QuickActionsSection(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "How’s your day going?",
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FilledTonalButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    navigator.navigate(JournalEntryRoute())
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = "Add",
                )
                Text(
                    text = "Add to Journal",
                )
            }
            FilledTonalButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    navigator.navigate(ContactActivityEditRoute(contactId = null, activityId = null))
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.Default.Stars,
                    contentDescription = "Add",
                )
                Text(
                    text = "Add Activity",
                )
            }
        }
    }
}
