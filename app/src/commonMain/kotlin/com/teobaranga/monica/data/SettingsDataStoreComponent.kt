package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

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
