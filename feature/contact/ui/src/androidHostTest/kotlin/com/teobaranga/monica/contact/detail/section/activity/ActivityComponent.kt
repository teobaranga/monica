package com.teobaranga.monica.contact.detail.section.activity

import com.teobaranga.monica.activity.data.ActivityApi
import com.teobaranga.monica.activity.data.MockActivityApiComponent
import com.teobaranga.monica.contact.data.MockContactApiComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface ActivityComponent: MockActivityApiComponent, MockContactApiComponent {

    val activityListViewModelFactory: ContactActivitiesViewModel.Factory

    fun activityApi(): ActivityApi
}
