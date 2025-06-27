package com.teobaranga.monica.core.datetime

import androidx.compose.runtime.staticCompositionLocalOf
import kotlin.time.Clock

val LocalSystemClock = staticCompositionLocalOf<Clock> { Clock.System }
