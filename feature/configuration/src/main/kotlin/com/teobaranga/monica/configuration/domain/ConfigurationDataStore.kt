package com.teobaranga.monica.configuration.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

interface ConfigurationDataStore {

    suspend fun <T> get(item: ConfigurationItem<T>): T

    fun <T> getAsFlow(item: ConfigurationItem<T>): Flow<T>

    suspend fun <T> set(item: ConfigurationItem<T>, value: T)
}

@Singleton
internal class ConfigurationDataStoreImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ConfigurationDataStore {

    private val Context.dataStore by preferencesDataStore(name = "configuration")

    private val dataStore = context.dataStore

    override suspend fun <T> get(item: ConfigurationItem<T>): T {
        return dataStore.data.first()[item.key] ?: item.default
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T> getAsFlow(item: ConfigurationItem<T>): Flow<T> {
        return dataStore.data.mapLatest { it[item.key] ?: item.default }
    }

    override suspend fun <T> set(item: ConfigurationItem<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[item.key] = value
        }
    }
}
