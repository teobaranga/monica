package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem.ShouldClearDatabaseOnNextLaunch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
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
}
