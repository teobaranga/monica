package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import javax.inject.Inject

class JournalNewEntrySynchronizer @Inject constructor(
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val newEntries = journalDao.getJournalEntriesByStatus(SyncStatus.NEW)

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

    private fun JournalEntry.toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            uuid = uuid,
            title = title,
            post = post,
            date = date,
            created = created,
            updated = updated,
            syncStatus = SyncStatus.UP_TO_DATE,
        )
    }
}
