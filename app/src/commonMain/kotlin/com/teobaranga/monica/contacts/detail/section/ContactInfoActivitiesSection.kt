package com.teobaranga.monica.contacts.detail.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teobaranga.monica.activity.list.ActivityListScreen
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection

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
