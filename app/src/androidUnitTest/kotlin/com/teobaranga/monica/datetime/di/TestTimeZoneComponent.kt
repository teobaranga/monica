package com.teobaranga.monica.datetime.di

import com.teobaranga.monica.core.datetime.di.TimeZoneComponent
import kotlinx.datetime.TimeZone
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

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
