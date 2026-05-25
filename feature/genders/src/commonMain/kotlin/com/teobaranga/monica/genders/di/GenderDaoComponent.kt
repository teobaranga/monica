package com.teobaranga.monica.genders.di

import com.teobaranga.monica.genders.data.GendersDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface GenderDaoComponent {

    @Provides
    fun providesGenderDao(owner: GenderTableOwner): GendersDao = owner.genderDao()
}
