package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.retrofit.serialization.deserializeErrorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import com.teobaranga.monica.data.common.DeleteResponse
import com.teobaranga.monica.data.common.ERROR_CODE_DATA_UNAVAILABLE
import com.teobaranga.monica.data.common.ErrorResponse
import com.teobaranga.monica.data.sync.SyncStatus
import kotlinx.serialization.SerializationException
import me.tatarka.inject.annotations.Inject
import timber.log.Timber

@Inject
class ContactDeleteSynchronizer(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        contactDao.getBySyncStatus(SyncStatus.DELETED)
            .forEach { deletedContact ->
                val contactId = deletedContact.contactId
                contactApi.deleteContact(contactId)
                    .suspendOnSuccess {
                        contactDao.delete(listOf(data.id))
                    }
                    .suspendOnError {
                        val error = try {
                            deserializeErrorBody<DeleteResponse, ErrorResponse>()?.error
                        } catch (e: SerializationException) {
                            Timber.e(e, "Error deserializing error response for contact $contactId")
                            null
                        }
                        if (error != null) {
                            Timber.e("Error deleting contact $contactId: ${error.errorCode} - ${error.message}")

                            if (error.errorCode == ERROR_CODE_DATA_UNAVAILABLE) {
                                // The contact has already been deleted
                                contactDao.delete(listOf(contactId))
                            }
                        }
                    }
            }
    }
}
