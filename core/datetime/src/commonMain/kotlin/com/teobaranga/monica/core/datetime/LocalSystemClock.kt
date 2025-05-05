package com.teobaranga.monica.core.datetime

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.Clock

val LocalSystemClock = staticCompositionLocalOf<Clock> { Clock.System }
