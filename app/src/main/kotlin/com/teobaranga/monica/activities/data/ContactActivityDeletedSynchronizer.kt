package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.ktor.bodyString
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.core.data.remote.ERROR_CODE_DATA_UNAVAILABLE
import com.teobaranga.monica.core.data.remote.ErrorResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import timber.log.Timber

@Inject
class ContactActivityDeletedSynchronizer(
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
    private val json: Json,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        contactActivitiesDao.getActivitiesByStatus(SyncStatus.DELETED)
            .forEach { deletedEntry ->
                val activityId = deletedEntry.activity.activityId
                contactApi.deleteActivity(activityId)
                    .suspendOnSuccess {
                        contactActivitiesDao.delete(listOf(data.id))
                    }
                    .suspendOnError {
                        val error = try {
                            json.decodeFromString<ErrorResponse>(bodyString()).error
                        } catch (e: SerializationException) {
                            Timber.e(e, "Error deserializing error response for activity $activityId")
                            null
                        }
                        if (error != null) {
                            Timber.e("Error deleting activity $activityId: ${error.errorCode} - ${error.message}")

                            if (error.errorCode == ERROR_CODE_DATA_UNAVAILABLE) {
                                // The activity has already been deleted
                                contactActivitiesDao.delete(listOf(activityId))
                            }
                        }
                    }
            }
    }
}
