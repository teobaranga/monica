package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module

@Module
abstract class DataStoreModule {

    @Binds
    abstract fun providePreferencesDataStore(dataStore: TestDataStore): DataStore<Preferences>
}
