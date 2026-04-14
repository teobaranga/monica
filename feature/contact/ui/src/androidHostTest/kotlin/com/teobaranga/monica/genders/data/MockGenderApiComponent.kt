package com.teobaranga.monica.genders.data

import io.mockk.mockk
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface MockGenderApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideGendersApi(): GendersApi = mockk<GendersApi>()
}
