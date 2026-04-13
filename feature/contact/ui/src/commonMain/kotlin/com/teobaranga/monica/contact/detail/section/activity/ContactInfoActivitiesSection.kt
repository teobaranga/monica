package com.teobaranga.monica.contact.detail.section.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.contact.detail.section.ContactInfoSection

data class ContactInfoActivitiesSection(
    private val contactId: Int,
) : ContactInfoSection {

    override val title: String = "Activities"

    @Composable
    override fun Content(modifier: Modifier) {
        ActivityListScreen(
            modifier = modifier,
            contactId = contactId,
        )
    }
}
