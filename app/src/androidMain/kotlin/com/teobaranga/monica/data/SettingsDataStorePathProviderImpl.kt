package com.teobaranga.monica.data

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import me.tatarka.inject.annotations.Inject
import okio.Path
import okio.Path.Companion.toPath
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

private const val PREFERENCES_DATASTORE_FILE_NAME = "datastore/settings.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class SettingsDataStorePathProviderImpl(
    @ApplicationContext
    private val context: Context,
) : SettingsDataStorePathProvider {

    override fun invoke(): Path {
        return context.filesDir.resolve(PREFERENCES_DATASTORE_FILE_NAME).absolutePath.toPath()
    }
}
