package com.teobaranga.monica.journal.data.remote

import io.mockk.mockk
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesTo(
    scope = AppScope::class,
    replaces = [JournalApiImpl::class],
)
interface TestJournalApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun journalApi(): JournalApi = mockk<JournalApi>()
}
