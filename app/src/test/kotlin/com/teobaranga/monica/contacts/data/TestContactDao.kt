package com.teobaranga.monica.contacts.data

import androidx.sqlite.db.SupportSQLiteQuery
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.data.DaosComponent
import com.teobaranga.monica.data.photo.ContactPhotos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    replaces = [DaosComponent::class],
)
class TestContactDao : ContactDao() {

    private val contacts = MutableStateFlow<MutableMap<Int, ContactEntity>>(mutableMapOf())

    override fun getContacts(query: SupportSQLiteQuery): Flow<List<ContactEntity>> {
        TODO("Not yet implemented")
    }

    override fun getContacts(entityIds: List<Int>): Flow<List<ContactEntity>> {
        synchronized(contacts) {
            return contacts.mapNotNull { contacts ->
                contacts.values.toList()
            }
        }
    }

    override fun getContactIds(): Flow<List<Int>> {
        TODO("Not yet implemented")
    }

    override fun getContact(id: Int): Flow<ContactEntity> {
        return contacts
            .mapNotNull { contacts ->
                contacts[id]
            }
    }

    override fun searchContacts(
        query: String,
        excludeIds: List<Int>,
    ): Flow<List<ContactEntity>> {
        TODO("Not yet implemented")
    }

    override fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertContacts(entities: List<ContactEntity>) {
        synchronized(contacts) {
            for (entity in entities) {
                contacts.value[entity.contactId] = entity
            }
        }
    }

    override suspend fun delete(entityIds: List<Int>) {
        synchronized(contacts) {
            for (id in entityIds) {
                contacts.value.remove(id)
            }
        }
    }

    override suspend fun getMaxId(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getBySyncStatus(status: SyncStatus): List<ContactEntity> {
        synchronized(contacts) {
            return contacts.value.values.filter { it.syncStatus == status }
        }
    }

    override suspend fun setSyncStatus(
        contactId: Int,
        syncStatus: SyncStatus,
    ) {
        synchronized(contacts) {
            contacts.value.computeIfPresent(contactId) { contactId, contact ->
                contact.copy(syncStatus = syncStatus)
            }
        }
    }
}
