package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.datetime.di.TimeZoneBindings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.datetime.TimeZone

val testTimeZone = TimeZone.of("Europe/London")

@ContributesTo(
    scope = AppScope::class,
    replaces = [TimeZoneBindings::class],
)
@BindingContainer
object TestTimeZoneBindings {

    @Provides
    fun timeZone(): TimeZone {
        return testTimeZone
    }
}
