package com.teobaranga.monica.activity.data

import io.mockk.mockk
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface MockActivityApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun mockActivityApi(): ActivityApi = mockk<ActivityApi>()
}
