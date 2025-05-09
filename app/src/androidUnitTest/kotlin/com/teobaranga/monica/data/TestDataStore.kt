package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File

@Inject
@SingleIn(AppScope::class)
class TestDataStore(
    private val dispatcher: Dispatcher,
) : DataStore<Preferences> {

    private val file = File("test.preferences_pb")

    private lateinit var prevScope: CoroutineScope

    private lateinit var dataStore: DataStore<Preferences>

    init {
        reset()
    }

    override val data: Flow<Preferences>
        get() = dataStore.data

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        return dataStore.updateData(transform)
    }

    /**
     * There can only be one active instance of a DataStore so we want to allow resetting it so that it
     * can be reused in tests.
     */
    fun reset() {
        if (::prevScope.isInitialized) {
            prevScope.cancel()
        }
        prevScope = CoroutineScope(dispatcher.io + SupervisorJob())
        dataStore = PreferenceDataStoreFactory.create(scope = prevScope) {
            file.delete()
            file
        }
    }
}
