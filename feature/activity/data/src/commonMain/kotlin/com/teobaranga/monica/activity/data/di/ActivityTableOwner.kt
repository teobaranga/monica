package com.teobaranga.monica.activity.data.di

import com.teobaranga.monica.activity.data.ContactActivitiesDao

interface ActivityTableOwner {

    fun activitiesDao(): ContactActivitiesDao
}
