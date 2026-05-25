package com.teobaranga.monica.contact

import com.teobaranga.monica.activity.data.ActivityApi
import com.teobaranga.monica.activity.data.MockActivityApiComponent
import com.teobaranga.monica.contact.data.MockContactApiComponent
import com.teobaranga.monica.contact.data.local.ContactDao
import com.teobaranga.monica.contact.data.remote.ContactApi
import com.teobaranga.monica.contact.edit.ContactEditViewModel
import com.teobaranga.monica.genders.data.GendersComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface ContactComponent: GendersComponent, MockContactApiComponent, MockActivityApiComponent {

    val contactEditViewModelFactory: ContactEditViewModel.Factory

    fun contactApi(): ContactApi

    fun activityApi(): ActivityApi

    fun contactDao(): ContactDao
}
