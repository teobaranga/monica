package com.teobaranga.monica.journal

import com.teobaranga.monica.core.data.remote.AccountResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import com.teobaranga.monica.journal.data.remote.JournalEntry
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.time.Month
import kotlin.uuid.Uuid

const val JOURNAL_ENTRY_ID_VALID = 1

val testLocalDate = LocalDate(2025, Month.APRIL, 26)

val validJournalEntry = JournalEntryEntity(
    id = JOURNAL_ENTRY_ID_VALID,
    title = "Title",
    post = "Post",
    date = testLocalDate,
    uuid = Uuid.random(),
    created = Instant.parse("2023-04-26T23:00:00Z"),
    updated = Instant.parse("2023-04-26T23:00:00Z"),
    syncStatus = SyncStatus.UP_TO_DATE,
)

fun JournalEntryEntity.toResponse(date: Instant): JournalEntry {
    return JournalEntry(
        id = id,
        uuid = uuid,
        account = AccountResponse(
            id = 1,
        ),
        title = title,
        post = post,
        date = date,
        created = created,
        updated = updated,
    )
}
