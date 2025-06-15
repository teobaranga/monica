package com.teobaranga.monica.data

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class IosDatabaseDeleter() : DatabaseDeleter {

    override fun deleteDatabase(databaseName: String) {
        // TODO
    }
}
