package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalSynchronizer @Inject constructor(
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
): Synchronizer {

    val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    override suspend fun sync() {
        syncState.value = Synchronizer.State.REFRESHING

        // Keep track of removed journal entries, start with the full database first
        val removedIds = journalDao.getJournalEntryIds().first().toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val journalEntriesResponse = journalApi.getJournal(page = nextPage, sort = "-updated_at")
                .onFailure {
                    Timber.w("Error while loading journal: %s", this)
                }
                .getOrNull() ?: break
            val journalEntries = journalEntriesResponse.data
                .map {
                    it.toEntity()
                }

            journalDao.upsertJournalEntries(journalEntries)

            journalEntriesResponse.meta.run {
                nextPage = if (currentPage != lastPage) {
                    currentPage + 1
                } else {
                    null
                }
            }

            // Reduce the list of entries to be removed based on the entries previously inserted
            removedIds -= journalEntries.map { it.id }.toSet()
        }

        journalDao.delete(removedIds.toList())

        syncState.value = Synchronizer.State.IDLE
    }
    
    private fun JournalEntryResponse.toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            uuid = uuid,
            accountId = account.id,
            title = title,
            post = post,
            date = date,
            created = created,
            updated = updated,
        )
    }
}
