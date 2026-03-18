package com.teobaranga.monica.activity

import com.teobaranga.monica.activity.data.ActivityApi
import com.teobaranga.monica.activity.list.ContactActivitiesViewModel
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface ActivityComponent {

    fun activityListViewModel(): ContactActivitiesViewModel.Factory

    fun activityApi(): ActivityApi
}
