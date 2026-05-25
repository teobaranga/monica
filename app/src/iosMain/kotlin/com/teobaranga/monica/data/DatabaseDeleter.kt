package com.teobaranga.monica.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
class IosDatabaseDeleter() : DatabaseDeleter {

    override fun deleteDatabase(databaseName: String) {
        // TODO
    }
}
