package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMonicaDatabase(@ApplicationContext context: Context): MonicaDatabase {
        return Room.databaseBuilder(context, MonicaDatabase::class.java, "monica")
            .build()
    }
}
