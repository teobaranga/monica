package com.teobaranga.monica.genders.di

import com.teobaranga.monica.genders.data.GendersDao
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface GenderDaoComponent {

    @Provides
    fun providesGenderDao(owner: GenderTableOwner): GendersDao = owner.genderDao()
}
