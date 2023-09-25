package com.teobaranga.monica.journal.data

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import com.teobaranga.monica.journal.database.toExternalModel
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class JournalRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val journalApi: JournalApi,
    private val journalDao: JournalDao,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private var needsSync: Boolean = true

    private fun syncJournal(orderBy: OrderBy? = null) {
        if (!needsSync) {
            return
        }
        needsSync = false
        scope.launch(dispatcher.io) {
            val sort = when (orderBy) {
                is OrderBy.Updated -> {
                    buildString {
                        if (!orderBy.isAscending) {
                            append("-")
                        }
                        append("updated_at")
                    }
                }
                is OrderBy.Date -> {
                    buildString {
                        if (!orderBy.isAscending) {
                            append("-")
                        }
                        append("date")
                    }
                }

                null -> null
            }

            var nextPage: Int? = 1
            while (nextPage != null) {
                val journalEntriesResponse = journalApi.getJournal(page = nextPage, sort = sort)
                    .onFailure {
                        Timber.w("Error while loading journal: %s", this)
                        (this as ApiResponse.Failure.Exception).exception
                    }
                    .getOrNull() ?: return@launch
                val contacts = journalEntriesResponse.data
                    .map(::mapResponse)
                journalDao.upsertJournalEntries(contacts)

                journalEntriesResponse.meta.run {
                    nextPage = if (currentPage != lastPage) {
                        currentPage + 1
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun getJournalEntries(orderBy: OrderBy? = null): Flow<List<JournalEntry>> {
        syncJournal()
        return journalDao.getJournalEntries(orderBy = orderBy?.let { OrderBy(it.columnName, it.isAscending) })
            .mapLatest { journalEntries ->
                journalEntries
                    .map {
                        it.toExternalModel()
                    }
            }
    }

    fun getJournalEntry(id: Int): Flow<JournalEntry> {
        return journalDao.getJournalEntry(id)
            .mapLatest {
                it.toExternalModel()
            }
    }

    private fun mapResponse(response: JournalEntryResponse): JournalEntryEntity {
        return JournalEntryEntity(
            id = response.id,
            uuid = response.uuid,
            accountId = response.account.id,
            title = response.title,
            post = response.post,
            date = response.date,
            created = response.created,
            updated = response.updated,
        )
    }

    sealed interface OrderBy {

        val columnName: String

        val isAscending: Boolean

        data class Updated(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(updated)"
        }

        data class Date(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(date)"
        }
    }
}
