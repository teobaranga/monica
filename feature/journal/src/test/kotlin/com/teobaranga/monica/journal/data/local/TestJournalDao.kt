package com.teobaranga.monica.journal.data.local

import androidx.sqlite.db.SupportSQLiteQuery
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.journal.di.JournalComponent
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
    replaces = [JournalComponent::class],
)
class TestJournalDao: JournalDao() {

    private val journalEntries = MutableStateFlow<MutableMap<Int, JournalEntryEntity>>(mutableMapOf())

    override fun getJournalEntries(query: SupportSQLiteQuery): Flow<List<JournalEntryEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getByStatus(status: SyncStatus): List<JournalEntryEntity> {
        synchronized(journalEntries) {
            return journalEntries.value.values.filter { it.syncStatus == status }.toList()
        }
    }

    override suspend fun setSyncStatus(
        entryId: Int,
        syncStatus: SyncStatus,
    ) {
        synchronized(journalEntries) {
            val updatedMap = journalEntries.value
            updatedMap.computeIfPresent(entryId) { _, entry ->
                entry.copy(syncStatus = syncStatus)
            }
            journalEntries.value = updatedMap
        }
    }

    override fun getJournalEntryIds(): Flow<List<Int>> {
        TODO("Not yet implemented")
    }

    override fun getJournalEntry(id: Int): Flow<JournalEntryEntity> {
        synchronized(journalEntries) {
            return journalEntries
                .mapNotNull { journalEntries ->
                    journalEntries[id]
                }
        }
    }

    override suspend fun upsertJournalEntries(entities: List<JournalEntryEntity>) {
        synchronized(journalEntries) {
            journalEntries.value += entities.associateBy { it.id }
        }
    }

    override suspend fun upsertJournalEntry(entry: JournalEntryEntity) {
        synchronized(journalEntries) {
            journalEntries.value[entry.id] = entry
        }
    }

    override suspend fun getMaxId(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entityIds: List<Int>) {
        TODO("Not yet implemented")
    }
}
