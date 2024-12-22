package com.teobaranga.monica.configuration.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.teobaranga.monica.inject.runtime.ApplicationContext
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
    @ApplicationContext context: Context,
) : ConfigurationDataStore {

    private val Context.dataStore by preferencesDataStore(name = "configuration")

    private val dataStore = context.dataStore

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
