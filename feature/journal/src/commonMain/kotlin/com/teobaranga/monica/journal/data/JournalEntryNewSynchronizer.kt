package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.remote.JournalApi
import com.teobaranga.monica.journal.data.remote.JournalEntryCreateRequest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
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
                    // Monica v4 original can't handle a missing or empty title,
                    // default to the date instead to avoid failing the request.
                    title = newEntry.title
                        ?: newEntry.date.format(LocalDate.Formats.ISO),
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
