package com.teobaranga.monica.data

import kotlinx.cinterop.ExperimentalForeignApi
import me.tatarka.inject.annotations.Inject
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

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
