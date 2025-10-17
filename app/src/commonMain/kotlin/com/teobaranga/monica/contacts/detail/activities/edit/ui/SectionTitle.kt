package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier.Companion,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelLarge,
    )
}
