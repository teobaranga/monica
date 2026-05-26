package com.teobaranga.monica.activity.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@ContributesTo(AppScope::class, replaces = [ActivityApi::class])
@BindingContainer
object MockActivityApiBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun mockActivityApi(): ActivityApi = mockk<ActivityApi>()
}
