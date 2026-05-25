package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface AndroidDatabaseBuilderComponent {

    @Provides
    fun providesMonicaDatabaseBuilder(@ApplicationContext context: Context): RoomDatabase.Builder<MonicaDatabase> {
        return Room.databaseBuilder(context, MonicaDatabase::class.java, DATABASE_NAME)
    }
}
