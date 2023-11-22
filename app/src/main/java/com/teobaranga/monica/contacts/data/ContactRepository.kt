package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.photo.ContactPhotos
import com.teobaranga.monica.data.photo.PhotoRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class ContactRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val pagingSource: Provider<ContactPagingSource.Factory>,
    private val photoRepository: PhotoRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

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

    fun getContacts(orderBy: OrderBy): ContactPagingSource {
        return pagingSource.get().create(orderBy)
    }

    fun getContact(id: Int): Flow<ContactEntity> {
        return contactDao.getContact(id)
    }

    fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        return contactDao.getContactPhotos(contactId)
    }

    private fun syncPhotos() {
        photoRepository.syncPhotos()
    }

    private fun mapContactResponse(contactResponse: ContactResponse): ContactEntity {
        syncPhotos()
        return ContactEntity(
            id = contactResponse.id,
            firstName = contactResponse.firstName,
            lastName = contactResponse.lastName,
            completeName = contactResponse.completeName,
            initials = contactResponse.initials,
            avatarUrl = contactResponse.info.avatar.url,
            avatarColor = contactResponse.info.avatar.color,
            updated = contactResponse.updated,
        )
    }

    sealed interface OrderBy {

        val columnName: String

        val isAscending: Boolean

        data class Name(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "completeName"
        }

        data class Updated(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(updated)"
        }
    }
}
