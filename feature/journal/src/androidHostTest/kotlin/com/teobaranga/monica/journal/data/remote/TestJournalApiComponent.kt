package com.teobaranga.monica.journal.data.remote

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@ContributesTo(
    scope = AppScope::class,
    replaces = [JournalApiImpl::class],
)
interface TestJournalApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun journalApi(): JournalApi = mockk<JournalApi>()
}
