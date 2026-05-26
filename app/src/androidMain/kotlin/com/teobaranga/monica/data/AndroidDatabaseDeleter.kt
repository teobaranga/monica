package com.teobaranga.monica.data

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
class AndroidDatabaseDeleter(
    @param:ApplicationContext
    private val context: Context,
) : DatabaseDeleter {

    override fun deleteDatabase(databaseName: String) {
        context.deleteDatabase(databaseName)
    }
}
