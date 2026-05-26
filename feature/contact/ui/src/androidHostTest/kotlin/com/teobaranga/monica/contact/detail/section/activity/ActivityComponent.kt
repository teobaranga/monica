package com.teobaranga.monica.contact.detail.section.activity

import com.teobaranga.monica.activity.data.ActivityApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface ActivityComponent {

    val activityListViewModelFactory: ContactActivitiesViewModel.Factory

    fun activityApi(): ActivityApi
}
