package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.paging.RoomPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class ContactPagingSource @AssistedInject constructor(
    dispatcher: Dispatcher,
    contactSynchronizer: ContactSynchronizer,
    private val contactDao: ContactDao,
    @Assisted
    private val orderBy: ContactRepository.OrderBy,
) : RoomPagingSource<Contact>(
    dispatcher = dispatcher,
    synchronizer = contactSynchronizer,
) {
    override suspend fun getEntries(start: Int, params: LoadParams<Int>): List<Contact> {
        return contactDao.getContacts(
            orderBy = with(orderBy) { OrderBy(columnName, isAscending) },
            limit = params.loadSize,
            offset = start * params.loadSize,
        ).map { contactEntities ->
            contactEntities.map {
                it.toExternalModel()
            }
        }.first()
    }

    @AssistedFactory
    internal interface Factory {
        fun create(orderBy: ContactRepository.OrderBy): ContactPagingSource
    }
}
