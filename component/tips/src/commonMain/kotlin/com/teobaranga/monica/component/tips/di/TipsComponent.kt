package com.teobaranga.monica.component.tips.di

import com.teobaranga.monica.component.tips.data.local.TipsDao
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface TipsComponent {

    @Provides
    fun providesTipsDao(owner: TipsTableOwner): TipsDao = owner.tipsDao()
}
