package com.teobaranga.monica.journal.data

import com.diamondedge.logging.logging
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.core.account.AccountListener
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.data.sync.Synchronizer
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import com.teobaranga.monica.journal.data.remote.JournalApi
import com.teobaranga.monica.journal.data.remote.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(AppScope::class)
@Inject
@ContributesBinding(AppScope::class, boundType = AccountListener::class, multibinding = true)
class JournalEntrySynchronizer(
    private val dispatcher: Dispatcher,
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
    private val journalEntryNewSynchronizer: JournalEntryNewSynchronizer,
    private val journalEntryUpdateSynchronizer: JournalEntryUpdateSynchronizer,
    private val journalEntryDeletedSynchronizer: JournalEntryDeletedSynchronizer,
) : Synchronizer, AccountListener {

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
                    log.e { "Error while loading journal: ${message()}" }
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

    override fun onSignedIn() {
        isSyncEnabled = true
    }

    companion object {

        private val log = logging()

        private var isSyncEnabled = true
    }
}

fun JournalEntry.toEntity(): JournalEntryEntity {
    return JournalEntryEntity(
        id = id,
        uuid = uuid,
        title = title,
        post = post,
        date = date.toLocalDateTime(TimeZone.currentSystemDefault()).date,
        created = created,
        updated = updated,
        syncStatus = SyncStatus.UP_TO_DATE,
    )
}
