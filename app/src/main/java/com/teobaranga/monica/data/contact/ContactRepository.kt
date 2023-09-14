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
            val multipleContactsResponse = contactApi.getContacts()
                .onFailure {
                    Timber.w("Error while loading contacts: %s", this)
                }
                .getOrNull() ?: return@launch
            val contacts = multipleContactsResponse.data
                .map(::mapContactResponse)
            contactDao.upsertContacts(contacts)
        }
    }

    fun syncContact(contactId: Int) {
        scope.launch(dispatcher.io) {
            val singleContactResponse = contactApi.getContact(contactId)
                .onFailure {
                    Timber.w("Error while loading contact %d: %s", contactId, this)
                }
                .getOrNull() ?: return@launch
            val contact = mapContactResponse(singleContactResponse.data)
            contactDao.upsertContacts(listOf(contact))
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
    
    private fun mapContactResponse(contactResponse: ContactResponse): ContactEntity {
        syncPhotos(contactResponse.id)
        return ContactEntity(
            id = contactResponse.id,
            firstName = contactResponse.firstName,
            lastName = contactResponse.lastName,
            initials = contactResponse.initials,
            avatarUrl = contactResponse.info.avatar.url,
            avatarColor = contactResponse.info.avatar.color,
        )
    }
}
