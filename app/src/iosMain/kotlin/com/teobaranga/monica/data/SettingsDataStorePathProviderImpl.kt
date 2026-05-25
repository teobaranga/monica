package com.teobaranga.monica.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

private const val PREFERENCES_DATASTORE_FILE_NAME = "settings.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class SettingsDataStorePathProviderImpl() : SettingsDataStorePathProvider {

    @OptIn(ExperimentalForeignApi::class)
    override fun invoke(): Path {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )

        return (requireNotNull(documentDirectory).path + "/$PREFERENCES_DATASTORE_FILE_NAME").toPath()
    }
}
