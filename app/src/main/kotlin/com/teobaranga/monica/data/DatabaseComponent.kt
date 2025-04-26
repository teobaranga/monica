package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem.ShouldClearDatabaseOnNextLaunch
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.journal.data.local.JournalDatabaseOwner
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun providesMonicaDatabase(
        @ApplicationContext context: Context,
        configurationDataStore: ConfigurationDataStore,
    ): MonicaDatabase {
        runBlocking {
            if (configurationDataStore.get(ShouldClearDatabaseOnNextLaunch)) {
                configurationDataStore.set(ShouldClearDatabaseOnNextLaunch, false)
                context.deleteDatabase("monica")
            }
        }
        return Room.databaseBuilder(context, MonicaDatabase::class.java, "monica")
            .build()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun providesRooomDatabase(database: MonicaDatabase): RoomDatabase = database

    @Provides
    @SingleIn(AppScope::class)
    fun providesJournalDatabaseOwner(database: MonicaDatabase): JournalDatabaseOwner = database
}
