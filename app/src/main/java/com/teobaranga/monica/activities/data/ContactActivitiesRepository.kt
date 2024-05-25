package com.teobaranga.monica.activities.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ContactActivitiesRepository @Inject constructor(
    private val contactActivitiesDao: ContactActivitiesDao,
) {

    fun getActivity(activityId: Int): Flow<ContactActivityEntity> {
        return contactActivitiesDao.getActivity(activityId)
    }
}
