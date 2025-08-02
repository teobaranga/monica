package com.teobaranga.monica.home

import androidx.navigation.NavGraphBuilder
import com.teobaranga.monica.certificate.nav.certificateListAndDetails
import com.teobaranga.monica.contacts.contacts
import com.teobaranga.monica.contacts.detail.activities.edit.ui.contactActivityEdit
import com.teobaranga.monica.contacts.detail.contactDetail
import com.teobaranga.monica.contacts.edit.contactEdit
import com.teobaranga.monica.dashboard.dashboard
import com.teobaranga.monica.journal.list.journalEntries
import com.teobaranga.monica.journal.view.journalEntry

val HomeNavGraphBuilder: NavGraphBuilder.() -> Unit = {
    dashboard()

    contacts()
    contactDetail()
    contactEdit()
    contactActivityEdit()

    journalEntries()
    journalEntry()

    certificateListAndDetails()
}
