package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import javax.inject.Inject

class JournalEntryDeletedSynchronizer @Inject constructor(
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
