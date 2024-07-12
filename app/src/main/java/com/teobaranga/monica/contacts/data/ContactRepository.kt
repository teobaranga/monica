package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.photo.ContactPhotos
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class ContactRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val pagingSource: Provider<ContactPagingSource.Factory>,
    private val contactNewSynchronizer: ContactNewSynchronizer,
    private val contactUpdateSynchronizer: ContactUpdateSynchronizer,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    fun syncContact(contactId: Int) {
        scope.launch(dispatcher.io) {
            val singleContactResponse = contactApi.getContact(contactId)
                .onFailure {
                    Timber.w("Error while loading contact %d: %s", contactId, this)
                }
                .getOrNull() ?: return@launch
            val contact = singleContactResponse.data.toEntity()
            contactDao.upsertContacts(listOf(contact))
        }
    }

    fun getContacts(orderBy: OrderBy): ContactPagingSource {
        return pagingSource.get().create(orderBy)
    }

    suspend fun searchContact(query: String): List<ContactEntity> {
        return contactDao.searchContacts(query)
    }

    fun getContacts(ids: List<Int>): Flow<List<ContactEntity>> {
        return contactDao.getContacts(ids)
    }

    fun getContact(id: Int): Flow<ContactEntity> {
        return contactDao.getContact(id)
    }

    fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        return contactDao.getContactPhotos(contactId)
    }

    suspend fun upsertContact(
        contactId: Int?,
        firstName: String,
        lastName: String?,
        nickname: String?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        if (contactId != null) {
            updateContact(contactId, firstName, lastName, nickname, birthdate)
        } else {
            insertContact(firstName, lastName, nickname, birthdate)
        }
    }

    private suspend fun insertContact(
        firstName: String,
        lastName: String?,
        nickname: String?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        val localId = contactDao.getMaxId() + 1
        val createdDate = OffsetDateTime.now()
        val entity = ContactEntity(
            contactId = localId,
            firstName = firstName,
            lastName = lastName,
            nickname = nickname,
            completeName = getCompleteName(firstName, lastName, nickname),
            initials = getInitials(firstName, lastName),
            birthdate = birthdate,
            updated = createdDate,
            // Avatar is set separately
            avatar = getRandomAvatar(),
            syncStatus = SyncStatus.NEW,
        )
        contactDao.upsertContacts(listOf(entity))
        scope.launch {
            contactNewSynchronizer.sync()
        }
    }

    private suspend fun updateContact(
        contactId: Int,
        firstName: String,
        lastName: String?,
        nickname: String?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        val originalContact = contactDao.getContact(contactId).firstOrNull() ?: return
        val updatedContact = originalContact.copy(
            firstName = firstName,
            lastName = lastName,
            nickname = nickname,
            completeName = getCompleteName(firstName, lastName, nickname),
            initials = getInitials(firstName, lastName),
            birthdate = birthdate,
            updated = OffsetDateTime.now(),
            syncStatus = SyncStatus.EDITED,
        )
        contactDao.upsertContacts(listOf(updatedContact))
        scope.launch {
            contactUpdateSynchronizer.sync()
        }
    }

    private fun getCompleteName(firstName: String, lastName: String?, nickname: String?): String {
        return buildString {
            append(firstName)
            if (lastName != null) {
                append(" ")
                append(lastName)
            }
            if (nickname != null) {
                append(" ")
                append("($nickname)")
            }
        }
    }

    private fun getInitials(firstName: String, lastName: String?): String {
        return buildString {
            append(firstName.first().uppercase())
            if (lastName != null) {
                append(lastName.first().uppercase())
            }
        }
    }

    private fun getRandomAvatar(): ContactEntity.Avatar {
        // List of colours should match the ones on the web
        val colors = listOf("#fdb660", "#93521e", "#bd5067", "#b3d5fe", "#ff9807", "#709512", "#5f479a", "#e5e5cd")
        return ContactEntity.Avatar(
            url = null,
            color = colors.random(),
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
