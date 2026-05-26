package com.teobaranga.monica.data

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import okio.Path
import okio.Path.Companion.toPath

private const val PREFERENCES_DATASTORE_FILE_NAME = "datastore/settings.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class SettingsDataStorePathProviderImpl(
    @param:ApplicationContext
    private val context: Context,
) : SettingsDataStorePathProvider {

    override fun invoke(): Path {
        return context.filesDir.resolve(PREFERENCES_DATASTORE_FILE_NAME).absolutePath.toPath()
    }
}
