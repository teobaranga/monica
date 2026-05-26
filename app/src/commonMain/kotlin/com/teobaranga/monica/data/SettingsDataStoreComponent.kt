package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface SettingsDataStoreComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun providesSettingsDataStore(pathProvider: SettingsDataStorePathProvider): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { pathProvider() }
        )
    }
}
