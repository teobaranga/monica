package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.remote.JournalApi
import me.tatarka.inject.annotations.Inject

@Inject
class JournalEntryDeletedSynchronizer(
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val deletedEntries = journalDao.getByStatus(SyncStatus.DELETED)

        for (entry in deletedEntries) {
            val entryId = entry.id
            when (val response = journalApi.deleteJournalEntry(entryId)) {
                is ApiResponse.Success -> {
                    journalDao.delete(listOf(entryId))
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
