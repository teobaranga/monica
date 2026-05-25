package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.datetime.di.TimeZoneComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.datetime.TimeZone

val testTimeZone = TimeZone.of("Europe/London")

@ContributesTo(
    scope = AppScope::class,
    replaces = [TimeZoneComponent::class],
)
interface TestTimeZoneComponent {

    @Provides
    fun timeZone(): TimeZone {
        return testTimeZone
    }
}
