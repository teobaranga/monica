package com.teobaranga.monica.activity.data.di

import com.teobaranga.monica.activity.data.ContactActivitiesDao
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface ActivityDaoComponent {


    @Provides
    fun providesContactActivitiesDao(owner: ActivityTableOwner): ContactActivitiesDao = owner.activitiesDao()
}
