package com.teobaranga.monica.activity.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@ContributesTo(AppScope::class)
interface MockActivityApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun mockActivityApi(): ActivityApi = mockk<ActivityApi>()
}
