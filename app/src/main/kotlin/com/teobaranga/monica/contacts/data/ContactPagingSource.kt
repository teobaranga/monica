package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.core.data.local.OrderBy
import com.teobaranga.monica.core.data.local.RoomPagingSource
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ContactPagingSource(
    dispatcher: Dispatcher,
    contactSynchronizer: ContactSynchronizer,
    private val contactDao: ContactDao,
    @Assisted
    private val orderBy: ContactRepository.OrderBy,
) : RoomPagingSource<ContactEntity>(
    dispatcher = dispatcher,
    synchronizer = contactSynchronizer,
) {
    override suspend fun getEntries(start: Int, params: LoadParams<Int>): List<ContactEntity> {
        return contactDao.getContacts(
            orderBy = with(orderBy) { OrderBy(columnName, isAscending) },
            limit = params.loadSize,
            offset = start * params.loadSize,
        ).first()
    }
}
