package com.teobaranga.monica.component.tips.di

import com.teobaranga.monica.component.tips.data.local.TipsDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface TipsComponent {

    @Provides
    fun providesTipsDao(owner: TipsTableOwner): TipsDao = owner.tipsDao()
}
