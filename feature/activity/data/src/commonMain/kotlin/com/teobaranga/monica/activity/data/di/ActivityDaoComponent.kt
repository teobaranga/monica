package com.teobaranga.monica.activity.data.di

import com.teobaranga.monica.activity.data.ContactActivitiesDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface ActivityDaoComponent {


    @Provides
    fun providesContactActivitiesDao(owner: ActivityTableOwner): ContactActivitiesDao = owner.activitiesDao()
}
