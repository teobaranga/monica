package com.teobaranga.monica.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.teobaranga.monica.activity.edit.ui.contactActivityEdit
import com.teobaranga.monica.certificate.nav.certificateListAndDetails
import com.teobaranga.monica.contact.detail.contactDetail
import com.teobaranga.monica.contact.edit.contactEdit
import com.teobaranga.monica.contacts.contacts
import com.teobaranga.monica.dashboard.dashboard
import com.teobaranga.monica.journal.list.journalEntries
import com.teobaranga.monica.journal.view.journalEntry

/**
 * Every destination of the app, each contributed by the feature that owns it.
 */
internal fun monicaEntryProvider(): (NavKey) -> NavEntry<NavKey> = entryProvider {
    dashboard()

    contacts()
    contactDetail()
    contactEdit()
    contactActivityEdit()

    journalEntries()
    journalEntry()

    certificateListAndDetails()
}
