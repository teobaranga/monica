package com.teobaranga.monica.contact.detail.section.activity

import com.teobaranga.monica.activity.data.ActivityApi
import com.teobaranga.monica.activity.data.MockActivityApiComponent
import com.teobaranga.monica.contact.data.MockContactApiComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface ActivityComponent: MockActivityApiComponent, MockContactApiComponent {

    fun activityListViewModel(): ContactActivitiesViewModel.Factory

    fun activityApi(): ActivityApi
}
