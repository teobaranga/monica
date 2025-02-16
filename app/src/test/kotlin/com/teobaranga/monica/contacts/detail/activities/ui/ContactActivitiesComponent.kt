package com.teobaranga.monica.contacts.detail.activities.ui

import com.teobaranga.monica.contacts.data.ContactApi
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface ContactActivitiesComponent {
    fun viewModel(): (contactId: Int) -> ContactActivitiesViewModel
    fun api(): ContactApi
}
