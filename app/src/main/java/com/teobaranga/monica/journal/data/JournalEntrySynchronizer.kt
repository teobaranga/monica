package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalEntrySynchronizer @Inject constructor(
    private val dispatcher: Dispatcher,
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
    private val journalEntryNewSynchronizer: JournalEntryNewSynchronizer,
    private val journalEntryUpdateSynchronizer: JournalEntryUpdateSynchronizer,
    private val journalEntryDeletedSynchronizer: JournalEntryDeletedSynchronizer,
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    suspend fun reSync() {
        withContext(dispatcher.io) {
            isSyncEnabled = true
            sync()
        }
    }

    override suspend fun sync() {
        if (!isSyncEnabled) {
            return
        }

        syncState.value = Synchronizer.State.REFRESHING

        journalEntryNewSynchronizer.sync()

        journalEntryUpdateSynchronizer.sync()

        journalEntryDeletedSynchronizer.sync()

        // Keep track of removed journal entries, start with the full database first
        val removedIds = journalDao.getJournalEntryIds().first().toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val journalEntriesResponse = journalApi.getJournal(page = nextPage, sort = "-updated_at")
                .onFailure {
                    Timber.w("Error while loading journal: %s", this)
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
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

        isSyncEnabled = false
    }

    companion object {

        private var isSyncEnabled = true
    }
}

fun JournalEntry.toEntity(): JournalEntryEntity {
    return JournalEntryEntity(
        id = id,
        uuid = uuid,
        title = title,
        post = post,
        date = date.toLocalDate(),
        created = created,
        updated = updated,
        syncStatus = SyncStatus.UP_TO_DATE,
    )
}
