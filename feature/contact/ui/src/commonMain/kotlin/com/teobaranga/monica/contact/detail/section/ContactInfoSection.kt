package com.teobaranga.monica.contact.detail.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ContactInfoSection {
    val title: String

    @Composable
    fun Content(modifier: Modifier)
}
