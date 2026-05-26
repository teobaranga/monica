package com.teobaranga.monica.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class, replaces = [AndroidDatabaseDeleter::class])
class FakeDatabaseDeleter: DatabaseDeleter {

    override fun deleteDatabase(databaseName: String) {
        // No-op
    }
}
