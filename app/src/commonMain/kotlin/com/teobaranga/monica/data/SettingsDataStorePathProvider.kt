package com.teobaranga.monica.data

import okio.Path

interface SettingsDataStorePathProvider {

    operator fun invoke(): Path
}
