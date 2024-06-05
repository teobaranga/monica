package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import javax.inject.Inject

class JournalEntryUpdateSynchronizer @Inject constructor(
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val editedEntries = journalDao.getByStatus(SyncStatus.EDITED)

        for (entry in editedEntries) {
            val response = journalApi.updateJournalEntry(
                id = entry.id,
                request = JournalEntryCreateRequest(
                    title = entry.title,
                    post = entry.post,
                    date = entry.date,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    journalDao.setSyncStatus(entry.id, SyncStatus.UP_TO_DATE)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
