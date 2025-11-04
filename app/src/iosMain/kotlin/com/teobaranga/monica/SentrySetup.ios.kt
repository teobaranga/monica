package com.teobaranga.monica

import kotlin.experimental.ExperimentalNativeApi

actual val sentryDsn: String =
    "https://c0f986968ef67872a5f0899cdb4cc1fb@o4510130255364096.ingest.de.sentry.io/4510171783757904"

@OptIn(ExperimentalNativeApi::class)
actual val sentryDebug: Boolean = Platform.isDebugBinary
