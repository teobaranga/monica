package com.teobaranga.monica.journal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.core.data.sync.Synchronizer
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.journal.data.JournalEntrySynchronizer
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import com.teobaranga.monica.journal.list.ui.JournalEntryListItem
import com.teobaranga.monica.user.data.local.IUserRepository
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import java.time.Year
import kotlin.time.Duration.Companion.seconds

private const val MAX_PREVIEW_CHARS = 300

@Inject
@ContributesViewModel(AppScope::class)
class JournalEntryListViewModel(
    private val dispatcher: Dispatcher,
    userRepository: IUserRepository,
    journalRepository: JournalRepository,
    private val journalEntrySynchronizer: JournalEntrySynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: UserAvatar.default(me.firstName)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val items = journalRepository.getJournalEntriesPagingData()
        .mapLatest { pagingData ->
            pagingData
                .map { journalEntryEntity ->
                    journalEntryEntity.toUiModel()
                }
                .insertSeparators { before, after ->
                    return@insertSeparators when {
                        before == null && after is JournalEntryListItem.Entry -> {
                            JournalEntryListItem.SectionTitle(
                                month = after.date.month,
                                year = null,
                            )
                        }

                        before is JournalEntryListItem.Entry && after is JournalEntryListItem.Entry -> {
                            if (before.date.month != after.date.month) {
                                JournalEntryListItem.SectionTitle(
                                    month = after.date.month,
                                    year = after.date.year.takeIf { it != Year.now().value },
                                )
                            } else {
                                JournalEntryListItem.Divider
                            }
                        }

                        else -> null
                    }
                }
        }
        .onStart {
            viewModelScope.launch(dispatcher.io) {
                journalEntrySynchronizer.sync()
            }
        }
        .cachedIn(viewModelScope)

    private val _refreshState = MonicaPullToRefreshState(onRefresh = ::refresh)
    val refreshState = journalEntrySynchronizer.syncState
        .mapLatest { state ->
            state == Synchronizer.State.REFRESHING
        }
        .map {
            _refreshState.apply {
                isRefreshing = it
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = _refreshState,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    private fun refresh() {
        viewModelScope.launch {
            journalEntrySynchronizer.reSync()
        }
    }

    private fun JournalEntryEntity.toUiModel(): JournalEntryListItem {
        return JournalEntryListItem.Entry(
            id = id,
            title = title,
            post = post
                // We don't need the full post
                .take(MAX_PREVIEW_CHARS)
                // Remove blank lines, they can show up as "..." and that's not useful
                .replace("\n\n", "\n"),
            date = date,
        )
    }
}
