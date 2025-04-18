package com.teobaranga.monica.contacts

import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.contacts.data.ContactDao
import com.teobaranga.monica.contacts.detail.activities.ui.ContactActivitiesViewModel
import com.teobaranga.monica.contacts.edit.ContactEditViewModel
import com.teobaranga.monica.genders.data.GendersComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface ContactComponent: GendersComponent {

    fun contactActivitiesViewModel(): (contactId: Int) -> ContactActivitiesViewModel

    fun contactEditViewModel(): (SavedStateHandle) -> ContactEditViewModel

    fun contactApi(): ContactApi

    fun contactDao(): ContactDao
}
