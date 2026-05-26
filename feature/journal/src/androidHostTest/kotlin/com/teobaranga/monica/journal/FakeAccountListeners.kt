package com.teobaranga.monica.journal

import com.teobaranga.monica.core.account.AccountListener
import com.teobaranga.monica.journal.data.JournalEntrySynchronizer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
/**
 * Exists to suppress "unused bindings" warning due to this set of [AccountListener] never being used in tests.
 */
@BindingContainer
@ContributesTo(AppScope::class, replaces = [JournalEntrySynchronizer::class])
object FakeAccountListeners {
    @Provides
    fun accountListeners(): Set<AccountListener> = emptySet()
}
