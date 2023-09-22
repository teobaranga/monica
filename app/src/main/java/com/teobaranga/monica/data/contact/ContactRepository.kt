package com.teobaranga.monica.data.contact

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.photo.ContactPhotos
import com.teobaranga.monica.data.photo.PhotoRepository
import com.teobaranga.monica.database.OrderBy
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

    private fun syncContacts(orderBy: OrderBy? = null) {
        if (!needsSync) {
            return
        }
        needsSync = false
        scope.launch(dispatcher.io) {
            val sort = when (orderBy) {
                is OrderBy.Updated -> {
                    buildString {
                        if (!orderBy.isAscending) {
                            append("-")
                        }
                        append("updated_at")
                    }
                }

                null -> null
            }

            var nextPage: Int? = 1
            while (nextPage != null) {
                val multipleContactsResponse = contactApi.getContacts(page = nextPage, sort = sort)
                    .onFailure {
                        Timber.w("Error while loading contacts: %s", this)
                    }
                    .getOrNull() ?: return@launch
                val contacts = multipleContactsResponse.data
                    .map(::mapContactResponse)
                contactDao.upsertContacts(contacts)

                multipleContactsResponse.meta.run {
                    if (currentPage != lastPage) {
                        nextPage = currentPage + 1
                    } else {
                        nextPage = null
                    }
                }
            }
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

    fun getContacts(orderBy: OrderBy? = null): Flow<List<ContactEntity>> {
        syncContacts()
        return contactDao.getContacts(orderBy = orderBy?.let { OrderBy(it.columnName, it.isAscending) })
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
            initials = contactResponse.initials,
            avatarUrl = contactResponse.info.avatar.url,
            avatarColor = contactResponse.info.avatar.color,
            updated = contactResponse.updated,
        )
    }

    sealed interface OrderBy {

        val columnName: String

        val isAscending: Boolean

        data class Updated(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(updated)"
        }
    }
}
