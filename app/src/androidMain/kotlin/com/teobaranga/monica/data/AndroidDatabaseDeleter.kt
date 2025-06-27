package com.teobaranga.monica.data

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

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
