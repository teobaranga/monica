package com.teobaranga.monica.contacts.data

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.photo.ContactPhotos
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.genders.domain.Gender
import com.teobaranga.monica.journal.data.ContactDeleteSynchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 10

@Singleton
internal class ContactRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val contactPagingSourceFactory: ContactPagingSource.Factory,
    private val contactNewSynchronizer: ContactNewSynchronizer,
    private val contactUpdateSynchronizer: ContactUpdateSynchronizer,
    private val contactDeleteSynchronizer: ContactDeleteSynchronizer,
    private val contactEntityMapper: ContactEntityMapper,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val pagingSourceFactoryMap = mutableMapOf<OrderBy, InvalidatingPagingSourceFactory<Int, ContactEntity>>()

    fun syncContact(contactId: Int) {
        scope.launch(dispatcher.io) {
            val singleContactResponse = contactApi.getContact(contactId)
                .onFailure {
                    Timber.e("Error while loading contact %d: %s", contactId, this)
                }
                .getOrNull() ?: return@launch
            val contact = contactEntityMapper(singleContactResponse.data)
            contactDao.upsertContacts(listOf(contact))
        }
    }

    fun getContactsPagingData(
        orderBy: OrderBy,
        config: PagingConfig = DefaultPagingConfig,
    ): Flow<PagingData<ContactEntity>> {
        val pagingSourceFactory = pagingSourceFactoryMap.getOrPut(orderBy) {
            InvalidatingPagingSourceFactory {
                contactPagingSourceFactory.create(orderBy)
            }
        }
        return Pager(
            config = config,
            pagingSourceFactory = pagingSourceFactory,
        ).flow
    }

    private fun invalidatePages() {
        pagingSourceFactoryMap.values.forEach { invalidatingPagingSourceFactory ->
            invalidatingPagingSourceFactory.invalidate()
        }
    }

    fun searchContact(query: String, excludeIds: List<Int> = emptyList()): Flow<List<ContactEntity>> {
        return contactDao.searchContacts(query, excludeIds)
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
        gender: Gender?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        if (contactId != null) {
            updateContact(contactId, firstName, lastName, nickname, gender, birthdate)
        } else {
            insertContact(firstName, lastName, nickname, gender, birthdate)
        }
    }

    private suspend fun insertContact(
        firstName: String,
        lastName: String?,
        nickname: String?,
        gender: Gender?,
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
            genderId = gender?.id,
            updated = createdDate,
            // Avatar is set separately
            avatar = getRandomAvatar(),
            syncStatus = SyncStatus.NEW,
        )
        contactDao.upsertContacts(listOf(entity))
        invalidatePages()
        scope.launch {
            contactNewSynchronizer.sync()
            invalidatePages()
        }
    }

    private suspend fun updateContact(
        contactId: Int,
        firstName: String,
        lastName: String?,
        nickname: String?,
        gender: Gender?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        val originalContact = contactDao.getContact(contactId).firstOrNull() ?: return
        val updatedContact = originalContact.copy(
            firstName = firstName,
            lastName = lastName,
            nickname = nickname,
            completeName = getCompleteName(firstName, lastName, nickname),
            initials = getInitials(firstName, lastName),
            genderId = gender?.id,
            birthdate = birthdate,
            updated = OffsetDateTime.now(),
            syncStatus = SyncStatus.EDITED,
        )
        contactDao.upsertContacts(listOf(updatedContact))
        invalidatePages()
        scope.launch {
            contactUpdateSynchronizer.sync()
            invalidatePages()
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

    suspend fun deleteContact(contactId: Int) {
        contactDao.setSyncStatus(contactId, SyncStatus.DELETED)
        invalidatePages()
        scope.launch {
            contactDeleteSynchronizer.sync()
            invalidatePages()
        }
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

    companion object {

        val DefaultPagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE,
        )
    }
}
