package com.teobaranga.monica.journal.data

import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.toExternalModel
import com.teobaranga.monica.journal.model.JournalEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
internal class JournalRepository @Inject constructor(
    private val journalDao: JournalDao,
    private val pagingSource: Provider<JournalPagingSource.Factory>,
) {
    fun getJournalEntries(orderBy: OrderBy): JournalPagingSource {
        return pagingSource.get().create(orderBy)
    }

    fun getJournalEntry(id: Int): Flow<JournalEntry> {
        return journalDao.getJournalEntry(id)
            .mapLatest {
                it.toExternalModel()
            }
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
