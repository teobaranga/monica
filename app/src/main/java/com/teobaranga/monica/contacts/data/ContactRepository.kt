package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.photo.ContactPhotos
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

    private fun mapContactResponse(contactResponse: ContactResponse): ContactEntity {
        return ContactEntity(
            contactId = contactResponse.id,
            firstName = contactResponse.firstName,
            lastName = contactResponse.lastName,
            nickname = contactResponse.nickname,
            completeName = contactResponse.completeName,
            initials = contactResponse.initials,
            avatar = ContactEntity.Avatar(
                url = contactResponse.info.avatar.url,
                color = contactResponse.info.avatar.color,
            ),
            birthdate = contactResponse.info.dates?.birthdate?.toBirthday(),
            updated = contactResponse.updated,
        )
    }

    private fun ContactResponse.Information.Dates.Birthdate.toBirthday(): ContactEntity.Birthdate? {
        return date?.let { date ->
            ContactEntity.Birthdate(
                isAgeBased = isAgeBased ?: false,
                isYearUnknown = isYearUnknown ?: false,
                date = date,
            )
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
}
