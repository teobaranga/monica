package com.teobaranga.monica.configuration.domain

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

interface ConfigurationDataStore {

    suspend fun <T> get(item: ConfigurationItem<T>): T

    fun <T> getAsFlow(item: ConfigurationItem<T>): Flow<T>

    suspend fun <T> set(item: ConfigurationItem<T>, value: T)
}

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class ConfigurationDataStoreImpl(
    dataStorePathProvider: ConfigurationDataStorePathProvider,
) : ConfigurationDataStore {

    private val dataStore = PreferenceDataStoreFactory.createWithPath(
        produceFile = { dataStorePathProvider() }
    )

    override suspend fun <T> get(item: ConfigurationItem<T>): T {
        return dataStore.data.first()[item.key] ?: item.default
    }

    override fun <T> getAsFlow(item: ConfigurationItem<T>): Flow<T> {
        return dataStore.data.mapLatest { it[item.key] ?: item.default }
    }

    override suspend fun <T> set(item: ConfigurationItem<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[item.key] = value
        }
    }
}
