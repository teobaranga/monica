package com.teobaranga.monica.data.contact

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.photo.PhotoRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val photoRepository: PhotoRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private var needsSync: Boolean = true

    fun syncContacts() {
        if (!needsSync) {
            return
        }
        needsSync = false
        scope.launch(dispatcher.io) {
            val contactsResponse = contactApi.getContacts()
                .onFailure {
                    Timber.w("Error while loading contacts: %s", this)
                }
                .getOrNull() ?: return@launch
            val contacts = contactsResponse.data
                .map {
                    syncPhotos(it.id)
                    ContactEntity(
                        id = it.id,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        initials = it.initials,
                        avatarUrl = it.info.avatar.url,
                        avatarColor = it.info.avatar.color,
                    )
                }
            contactDao.upsertContacts(contacts)
        }
    }

    fun getContacts(): Flow<List<ContactEntity>> {
        return contactDao.getContacts()
    }

    fun getContact(id: Int): Flow<ContactEntity> {
        return contactDao.getContact(id)
    }

    private fun syncPhotos(contactId: Int) {
        photoRepository.syncPhotos(contactId)
    }
}
