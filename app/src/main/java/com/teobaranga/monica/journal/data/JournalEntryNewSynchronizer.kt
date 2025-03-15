package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import me.tatarka.inject.annotations.Inject

@Inject
class JournalEntryNewSynchronizer(
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val newEntries = journalDao.getByStatus(SyncStatus.NEW)

        for (newEntry in newEntries) {
            val response = journalApi.createJournalEntry(
                JournalEntryCreateRequest(
                    title = newEntry.title.orEmpty(),
                    post = newEntry.post,
                    date = newEntry.date,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    val entity = response.data.data.toEntity()
                    journalDao.sync(newEntry.id, entity)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
