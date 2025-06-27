package com.teobaranga.monica.core.datetime.di

import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.core.datetime.Year
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import kotlin.time.Clock
import kotlin.time.Instant

@ContributesTo(AppScope::class)
interface DateTimeComponent {

    @Provides
    fun now(clock: Clock): () -> Instant = { clock.now() }

    @Provides
    fun nowLocalDate(clock: Clock, timeZone: TimeZone): () -> LocalDate = { clock.todayIn(timeZone) }

    @Provides
    fun nowYear(getNowLocalDate: () -> LocalDate): () -> Year = { getNowLocalDate().year }

    @Provides
    fun nowMonthDay(getNowLocalDate: () -> LocalDate): () -> MonthDay = { MonthDay.from(getNowLocalDate()) }
}
