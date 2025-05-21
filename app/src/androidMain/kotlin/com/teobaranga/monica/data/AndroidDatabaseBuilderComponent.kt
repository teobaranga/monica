package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teobaranga.monica.core.inject.ApplicationContext
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface AndroidDatabaseBuilderComponent {

    @Provides
    fun providesMonicaDatabaseBuilder(@ApplicationContext context: Context): RoomDatabase.Builder<MonicaDatabase> {
        return Room.databaseBuilder(context, MonicaDatabase::class.java, DATABASE_NAME)
    }
}
