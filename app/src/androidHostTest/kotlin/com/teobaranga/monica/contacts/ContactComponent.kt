package com.teobaranga.monica.contacts

import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.activity.data.ActivityApi
import com.teobaranga.monica.contact.data.remote.ContactApi
import com.teobaranga.monica.contacts.data.ContactDao
import com.teobaranga.monica.contacts.edit.ContactEditViewModel
import com.teobaranga.monica.genders.data.GendersComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface ContactComponent: GendersComponent {

    fun contactEditViewModel(): (SavedStateHandle) -> ContactEditViewModel

    fun contactApi(): ContactApi

    fun activityApi(): ActivityApi

    fun contactDao(): ContactDao
}
