package com.teobaranga.monica.data

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.NativeSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface DatabaseBuilderComponent {

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }

    @Provides
    fun providesMonicaDatabaseBuilder(): RoomDatabase.Builder<MonicaDatabase> {
        val dbFilePath = documentDirectory() + "/monica.db"
        return Room.databaseBuilder<MonicaDatabase>(name = dbFilePath)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .setDriver(NativeSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)

    }
}
