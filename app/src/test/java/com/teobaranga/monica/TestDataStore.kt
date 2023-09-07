package com.teobaranga.monica

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.nio.file.Files
import kotlin.io.path.deleteIfExists

@OptIn(ExperimentalCoroutinesApi::class)
fun testDataStore(): Lazy<DataStore<Preferences>> = lazy {
    val tempFile = Files.createTempFile("preferences", ".preferences_pb")
    PreferenceDataStoreFactory.create(
        scope = CoroutineScope(UnconfinedTestDispatcher()),
        produceFile = {
            // The DataStore implementation uses File.renameTo to replace the main database file with the scratch
            // one after an update. This doesn't work on Windows, the call fails if the file already exists. The
            // line below is a workaround that just deletes the main file so that the rename succeeds.
            if (System.getProperty("os.name").orEmpty().lowercase().contains("windows")) {
                tempFile.deleteIfExists()
            }
            tempFile.toFile().apply {
                deleteOnExit()
            }
        },
    )
}
