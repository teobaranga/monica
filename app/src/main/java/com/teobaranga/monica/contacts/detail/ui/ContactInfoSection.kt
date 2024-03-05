package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed interface ContactInfoSection {
    val title: String

    @Composable
    fun Content(modifier: Modifier)
}
