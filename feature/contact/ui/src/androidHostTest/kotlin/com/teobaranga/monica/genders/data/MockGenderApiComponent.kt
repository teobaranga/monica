package com.teobaranga.monica.genders.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@ContributesTo(AppScope::class, replaces = [GendersApiImpl::class])
interface MockGenderApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideGendersApi(): GendersApi = mockk<GendersApi>()
}
