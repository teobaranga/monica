package com.teobaranga.monica.contacts.detail.bio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun BioItem(icon: @Composable () -> Unit, title: String, description: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        icon()
        Column(
            modifier = Modifier
                .padding(start = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
